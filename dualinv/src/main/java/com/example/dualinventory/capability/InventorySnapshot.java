package com.example.dualinventory.capability;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/** A plain-data snapshot of main inventory, armor, offhand, and optional modded slots. */
public class InventorySnapshot {
    public static final int ARMOR_SIZE = 4;
    public List<ItemStack> main = new ArrayList<>();
    public final NonNullList<ItemStack> armor = NonNullList.withSize(ARMOR_SIZE, ItemStack.EMPTY);
    public ItemStack offhand = ItemStack.EMPTY;
    public ItemStack travelersBackpack = ItemStack.EMPTY;
    public List<ItemStack> curiosData = new ArrayList<>();

    public void captureFrom(Inventory inv) {
        int liveMainSize = inv.items.size();
        main = new ArrayList<>(liveMainSize);
        for (int i = 0; i < liveMainSize; i++) main.add(inv.items.get(i).copy());
        for (int i = 0; i < ARMOR_SIZE; i++) armor.set(i, inv.armor.get(i).copy());
        offhand = inv.offhand.get(0).copy();
    }

    public void applyTo(Inventory inv) {
        int liveMainSize = inv.items.size();
        for (int i = 0; i < liveMainSize; i++) {
            ItemStack toSet = i < main.size() ? main.get(i).copy() : ItemStack.EMPTY;
            inv.items.set(i, toSet);
        }
        for (int i = 0; i < ARMOR_SIZE; i++) inv.armor.set(i, armor.get(i).copy());
        inv.offhand.set(0, offhand.copy());
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.put("Main", writeList(main));
        tag.putInt("MainSize", main.size());
        tag.put("Armor", writeList(armor));
        tag.put("Offhand", offhand.save(new CompoundTag()));
        if (!travelersBackpack.isEmpty()) tag.put("TravelersBackpack", travelersBackpack.save(new CompoundTag()));
        if (!curiosData.isEmpty()) {
            tag.put("Curios", writeList(curiosData));
            tag.putInt("CuriosSize", curiosData.size());
        }
        return tag;
    }

    public void load(CompoundTag tag) {
        int savedMainSize = tag.contains("MainSize") ? tag.getInt("MainSize") : 36;
        main = new ArrayList<>(savedMainSize);
        for (int i = 0; i < savedMainSize; i++) main.add(ItemStack.EMPTY);
        readList(tag.getList("Main", 10), main);
        readList(tag.getList("Armor", 10), armor);
        offhand = tag.contains("Offhand") ? ItemStack.of(tag.getCompound("Offhand")) : ItemStack.EMPTY;
        travelersBackpack = tag.contains("TravelersBackpack") ? ItemStack.of(tag.getCompound("TravelersBackpack")) : ItemStack.EMPTY;
        int curiosSize = tag.contains("CuriosSize") ? tag.getInt("CuriosSize") : 0;
        curiosData = new ArrayList<>(curiosSize);
        for (int i = 0; i < curiosSize; i++) curiosData.add(ItemStack.EMPTY);
        readList(tag.getList("Curios", 10), curiosData);
    }

    private static ListTag writeList(List<ItemStack> list) {
        ListTag listTag = new ListTag();
        for (int i = 0; i < list.size(); i++) {
            ItemStack stack = list.get(i);
            if (stack.isEmpty()) continue;
            CompoundTag entry = new CompoundTag();
            entry.putInt("Slot", i);
            stack.save(entry);
            listTag.add(entry);
        }
        return listTag;
    }

    private static void readList(ListTag listTag, List<ItemStack> target) {
        for (int i = 0; i < target.size(); i++) target.set(i, ItemStack.EMPTY);
        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag entry = listTag.getCompound(i);
            int slot = entry.getInt("Slot");
            if (slot >= 0 && slot < target.size()) target.set(slot, ItemStack.of(entry));
        }
    }
}
