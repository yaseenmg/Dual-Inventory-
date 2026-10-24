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

public final class InventorySwapService {
    private InventorySwapService() {}

    public static void swap(ServerPlayer player) {
        DualInventoryData data = CapabilityHandler.get(player);
        DualInventoryData.Slot leaving = data.getActive();
        DualInventoryData.Slot entering = data.other();
        data.get(leaving).captureFrom(player.getInventory());
        data.get(entering).applyTo(player.getInventory());

        if (ModList.get().isLoaded("travelersbackpack")) {
            data.get(leaving).travelersBackpack = TravelersBackpackCompat.capture(player);
            TravelersBackpackCompat.apply(player, data.get(entering).travelersBackpack);
        }
        if (ModList.get().isLoaded("curios")) {
            data.get(leaving).curiosData = CuriosCompat.capture(player);
            CuriosCompat.apply(player, data.get(entering).curiosData);
        }

        data.setActive(entering);
        player.getInventory().setChanged();
        player.containerMenu.sendAllDataToRemote();
        playEquipSounds(player);
        sendSwitchMessage(player, entering);
    }

    private static void playEquipSounds(ServerPlayer player) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.ARMOR) continue;
            ItemStack stack = player.getItemBySlot(slot);
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof ArmorItem armorItem) {
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        armorItem.getMaterial().getEquipSound(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
    }

    private static void sendSwitchMessage(ServerPlayer player, DualInventoryData.Slot active) {
        String label = active == DualInventoryData.Slot.A ? "A" : "B";
        player.displayClientMessage(Component.translatable("dualinventory.switched", label), true);
    }
}
