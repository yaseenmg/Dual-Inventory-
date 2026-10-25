package com.example.dualinventory.compat;

import com.tiviacz.travelersbackpack.capability.CapabilityUtils;
import com.tiviacz.travelersbackpack.capability.ITravelersBackpack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Optional integration with Traveler's Backpack (soft dependency - this class is only ever
 * referenced from InventorySwapService behind a `ModList.isLoaded("travelersbackpack")` check,
 * so the JVM never attempts to load/verify it, and never throws NoClassDefFoundError, when
 * that mod isn't installed).
 * <p>
 * Traveler's Backpack stores the currently-worn backpack in its OWN capability
 * (ITravelersBackpack), completely separate from the vanilla armor/inventory slots - this is
 * why a worn backpack was previously untouched by A/B swaps. Verified directly from the mod's
 * own bytecode (Krakatau disassembly of travelersbackpack-forge-1.20.1-9.1.54.jar):
 *   - CapabilityUtils.getCapability(Player) -> LazyOptional<ITravelersBackpack>
 *   - ITravelersBackpack.getBackpack()      -> the currently worn backpack ItemStack (or EMPTY)
 *   - ITravelersBackpack.equipBackpack(ItemStack) -> removes whatever is currently worn first,
 *     then equips the given stack (a no-op equip if it isn't a backpack item, e.g. EMPTY),
 *     and synchronises to the client automatically. One call safely handles "swap to nothing"
 *     and "swap to a different backpack" alike.
 */
public final class TravelersBackpackCompat {

    private TravelersBackpackCompat() {}

    /** Returns a copy of the currently worn backpack, or ItemStack.EMPTY if none is worn. */
    public static ItemStack capture(Player player) {
        return CapabilityUtils.getCapability(player)
                .resolve()
                .map(ITravelersBackpack::getBackpack)
                .orElse(ItemStack.f_41583_)
                .m_41777_();
    }

    /** Equips the given backpack stack (or removes the worn one if it's empty/not a backpack). */
    public static void apply(Player player, ItemStack backpack) {
        CapabilityUtils.getCapability(player)
                .resolve()
                .ifPresent(cap -> cap.equipBackpack(backpack.m_41777_()));
    }
}
