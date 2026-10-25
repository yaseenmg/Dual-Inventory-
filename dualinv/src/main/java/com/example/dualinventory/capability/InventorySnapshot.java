package com.example.dualinventory.capability;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * A plain-data snapshot of everything a swap needs to cover: the main/hotbar slots (size
 * read dynamically - NOT hardcoded to vanilla's 36, so mods that resize the player's real
 * Inventory.items list, e.g. some backpack/expanded-hotbar mods, are fully supported), the
 * 4 armor slots, and the offhand slot.
 * <p>
 * Every vanilla member below is the exact SRG identifier, verified directly via `javap`
 * against the real client-1.20.1-47.4.10 SRG jar and Mojang's official mappings:
 *   Inventory.items   -> f_35974_
 *   Inventory.armor   -> f_35975_
 *   Inventory.offhand -> f_35976_
 *   ItemStack.isEmpty()      -> m_41619_
 *   ItemStack.copy()         -> m_41777_
 *   ItemStack.save(Tag)      -> m_41739_
 *   ItemStack.of(Tag) static -> m_41712_
 *   CompoundTag.put          -> m_128365_
 *   CompoundTag.putInt       -> m_128405_
 *   CompoundTag.getInt       -> m_128451_
 *   CompoundTag.getCompound  -> m_128469_
 *   CompoundTag.getList      -> m_128437_
 */
public class InventorySnapshot {

    public static final int ARMOR_SIZE = 4;

    /**
     * Not a fixed-size NonNullList anymore: sized dynamically to whatever the live
     * Inventory.items actually contains at capture time (see captureFrom). A plain
     * ArrayList is fine here since this is purely our own save/restore buffer, never
     * passed back into vanilla code expecting NonNullList's non-null guarantee.
     */
    public List<ItemStack> main = new ArrayList<>();

    public final NonNullList<ItemStack> armor = NonNullList.m_122780_(ARMOR_SIZE, ItemStack.f_41583_);
    public ItemStack offhand = ItemStack.f_41583_;

    /**
     * The worn Traveler's Backpack (with all its contents preserved in its own NBT), if that
     * mod is installed and the player was wearing one at capture time. Stays ItemStack.EMPTY
     * (and is simply ignored) when the mod isn't present.
     */
    public ItemStack travelersBackpack = ItemStack.f_41583_;

    /** Contents of every Curios slot, in slot order, if the Curios mod is installed. */
    public List<ItemStack> curiosData = new ArrayList<>();

    /** Copies the live contents of the player's vanilla inventory into this snapshot. */
    public void captureFrom(Inventory inv) {
        int liveMainSize = inv.f_35974_.size();
        main = new ArrayList<>(liveMainSize);
        for (int i = 0; i < liveMainSize; i++) {
            main.add(inv.f_35974_.get(i).m_41777_());
        }
        for (int i = 0; i < ARMOR_SIZE; i++) {
            armor.set(i, inv.f_35975_.get(i).m_41777_());
        }
        offhand = inv.f_35976_.get(0).m_41777_();
    }

    /**
     * Writes this snapshot's contents into the player's live vanilla inventory. Every live
     * main-inventory slot is explicitly written - either to the saved item, or to EMPTY if
     * this snapshot doesn't have data for that slot (e.g. a completely fresh/never-captured
     * snapshot, or a backpack mod added slots after this snapshot was last saved). This is
     * deliberate: a slot must never be left holding whatever was there before the swap.
     */
    public void applyTo(Inventory inv) {
        int liveMainSize = inv.f_35974_.size();
        for (int i = 0; i < liveMainSize; i++) {
            ItemStack toSet = i < main.size() ? main.get(i).m_41777_() : ItemStack.f_41583_;
            inv.f_35974_.set(i, toSet);
        }
        for (int i = 0; i < ARMOR_SIZE; i++) {
            inv.f_35975_.set(i, armor.get(i).m_41777_());
        }
        inv.f_35976_.set(0, offhand.m_41777_());
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.m_128365_("Main", writeList(main));
        tag.m_128405_("MainSize", main.size());
        tag.m_128365_("Armor", writeList(armor));
        tag.m_128365_("Offhand", offhand.m_41739_(new CompoundTag()));
        if (!travelersBackpack.m_41619_()) {
            tag.m_128365_("TravelersBackpack", travelersBackpack.m_41739_(new CompoundTag()));
        }
        if (!curiosData.isEmpty()) {
            tag.m_128365_("Curios", writeList(curiosData));
            tag.m_128405_("CuriosSize", curiosData.size());
        }
        return tag;
    }

    public void load(CompoundTag tag) {
        int savedMainSize = tag.m_128441_("MainSize") ? tag.m_128451_("MainSize") : 36;
        main = new ArrayList<>(savedMainSize);
        for (int i = 0; i < savedMainSize; i++) {
            main.add(ItemStack.f_41583_);
        }
        readList(tag.m_128437_("Main", 10), main);
        readList(tag.m_128437_("Armor", 10), armor);
        offhand = tag.m_128441_("Offhand") ? ItemStack.m_41712_(tag.m_128469_("Offhand")) : ItemStack.f_41583_;
        travelersBackpack = tag.m_128441_("TravelersBackpack")
                ? ItemStack.m_41712_(tag.m_128469_("TravelersBackpack"))
                : ItemStack.f_41583_;

        int curiosSize = tag.m_128441_("CuriosSize") ? tag.m_128451_("CuriosSize") : 0;
        curiosData = new ArrayList<>(curiosSize);
        for (int i = 0; i < curiosSize; i++) {
            curiosData.add(ItemStack.f_41583_);
        }
        readList(tag.m_128437_("Curios", 10), curiosData);
    }

    private static ListTag writeList(List<ItemStack> list) {
        ListTag listTag = new ListTag();
        for (int i = 0; i < list.size(); i++) {
            ItemStack stack = list.get(i);
            if (stack.m_41619_()) continue;
            CompoundTag entry = new CompoundTag();
            entry.m_128405_("Slot", i);
            stack.m_41739_(entry);
            listTag.add(entry);
        }
        return listTag;
    }

    private static void readList(ListTag listTag, List<ItemStack> target) {
        // Clear first: load() may run on an already-populated snapshot (e.g. a capability
        // re-sync), and this list only contains entries for non-empty slots (see writeList) -
        // without this, a slot that became empty since the last save would incorrectly keep
        // its old, stale item instead of turning empty.
        for (int i = 0; i < target.size(); i++) {
            target.set(i, ItemStack.f_41583_);
        }
        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag entry = listTag.m_128728_(i);
            int slot = entry.m_128451_("Slot");
            if (slot >= 0 && slot < target.size()) {
                target.set(slot, ItemStack.m_41712_(entry));
            }
        }
    }
}

