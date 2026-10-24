package com.example.dualinventory.compat;

import com.tiviacz.travelersbackpack.capability.CapabilityUtils;
import com.tiviacz.travelersbackpack.capability.ITravelersBackpack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/** Optional integration with Traveler's Backpack; loaded only when the mod is present. */
public final class TravelersBackpackCompat {
    private TravelersBackpackCompat() {}

    public static ItemStack capture(Player player) {
        return CapabilityUtils.getCapability(player)
                .resolve()
                .map(ITravelersBackpack::getBackpack)
                .orElse(ItemStack.EMPTY)
                .copy();
    }

    public static void apply(Player player, ItemStack backpack) {
        CapabilityUtils.getCapability(player)
                .resolve()
                .ifPresent(cap -> cap.equipBackpack(backpack.copy()));
    }
}
