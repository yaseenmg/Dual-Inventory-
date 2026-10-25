package com.example.dualinventory.capability;

import com.example.dualinventory.compat.CuriosCompat;
import com.example.dualinventory.compat.TravelersBackpackCompat;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;

/**
 * Every vanilla member below is the exact SRG identifier verified via `javap` against the
 * real client-1.20.1-47.4.10 SRG jar:
 *   Player.getInventory()                 -> m_150109_
 *   Player.inventoryMenu (field)          -> f_36095_
 *   Inventory.setChanged()                -> m_6596_
 *   ArmorItem.getMaterial()               -> m_40401_
 *   ArmorMaterial.getEquipSound()         -> m_7344_
 *   LivingEntity.getItemBySlot(slot)      -> m_6844_
 *   Entity.getX()/getY()/getZ()           -> m_20185_ / m_20186_ / m_20189_
 *   Entity.level()                        -> m_9236_
 *   Level.playSound(Player,d,d,d,SE,SS,f,f) -> m_6263_
 *   ServerPlayer.displayClientMessage     -> m_5661_
 *   AbstractContainerMenu.broadcastChanges -> m_38946_
 *   Component.translatable(String,Object...) -> m_237110_ (NOT m_237115_, which is the no-args overload)
 */
public final class InventorySwapService {

    private InventorySwapService() {}

    public static void swap(ServerPlayer player) {
        DualInventoryData data = CapabilityHandler.get(player);

        DualInventoryData.Slot leaving = data.getActive();
        DualInventoryData.Slot entering = data.other();

        data.get(leaving).captureFrom(player.m_150109_());
        data.get(entering).applyTo(player.m_150109_());

        // Soft dependency: TravelersBackpackCompat is only ever referenced here, behind this
        // isLoaded check, so the JVM never loads/verifies it (and never throws
        // NoClassDefFoundError) when Traveler's Backpack isn't installed.
        if (ModList.get().isLoaded("travelersbackpack")) {
            data.get(leaving).travelersBackpack = TravelersBackpackCompat.capture(player);
            TravelersBackpackCompat.apply(player, data.get(entering).travelersBackpack);
        }

        if (ModList.get().isLoaded("curios")) {
            data.get(leaving).curiosData = CuriosCompat.capture(player);
            CuriosCompat.apply(player, data.get(entering).curiosData);
        }

        data.setActive(entering);

        player.m_150109_().m_6596_();
        player.f_36096_.m_150429_(); // containerMenu.sendAllDataToRemote() - forces a full,
                                     // unconditional resync to the client instead of relying
                                     // on broadcastChanges()'s diff-based detection, which can
                                     // miss changes made by directly mutating the inventory
                                     // array (as this mod does) instead of through normal
                                     // slot-click code paths.

        playEquipSounds(player);
        sendSwitchMessage(player, entering);
    }

    private static void playEquipSounds(ServerPlayer player) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.m_20743_() != EquipmentSlot.Type.ARMOR) continue;
            ItemStack stack = player.m_6844_(slot);
            if (stack.m_41619_()) continue;
            if (stack.m_41720_() instanceof ArmorItem armorItem) {
                player.m_9236_().m_6263_(
                        null,
                        player.m_20185_(), player.m_20186_(), player.m_20189_(),
                        armorItem.m_40401_().m_7344_(),
                        SoundSource.PLAYERS,
                        1.0F, 1.0F
                );
            }
        }
    }

    private static void sendSwitchMessage(ServerPlayer player, DualInventoryData.Slot active) {
        String label = active == DualInventoryData.Slot.A ? "A" : "B";
        player.m_5661_(Component.m_237110_("dualinventory.switched", label), true);
    }
}
