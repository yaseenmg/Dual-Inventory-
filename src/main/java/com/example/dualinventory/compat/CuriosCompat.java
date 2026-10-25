package com.example.dualinventory.compat;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Optional integration with Curios (soft dependency - only ever referenced from
 * InventorySwapService behind a `ModList.isLoaded("curios")` check).
 * <p>
 * IMPORTANT: this mutates curio slots one-by-one through the standard Forge
 * IItemHandlerModifiable interface (getStackInSlot/setStackInSlot) - the SAME interface
 * Curios' own equip/unequip UI uses internally - instead of Curios' own bulk
 * writeTag()/readTag() capability methods. An earlier version of this integration used
 * writeTag()/readTag() directly, which changes the server-side data correctly but does NOT
 * trigger Curios' internal client sync packet (SPacketSyncCurios is only sent in response to
 * specific equip/unequip/change code paths - readTag() alone doesn't go through them), causing
 * a visible desync: the worn curio cosmetic model wouldn't update until some unrelated action
 * (like opening the inventory screen) forced a refresh. Going through setStackInSlot instead is
 * expected to trigger the same internal change-detection Curios' own UI relies on.
 * <p>
 * Verified directly from Curios' own bytecode (curios-forge-5.14.1+1.20.1.jar):
 *   CuriosApi.getCuriosInventory(LivingEntity)   -> LazyOptional<ICuriosItemHandler>
 *   ICuriosItemHandler.getEquippedCurios()       -> IItemHandlerModifiable (all curio slots, flattened)
 * IItemHandlerModifiable/IItemHandler are standard Forge interfaces (not obfuscated,
 * verified via javap against forge-1.20.1-47.4.10-universal.jar):
 *   getSlots(), getStackInSlot(int), setStackInSlot(int, ItemStack)
 */
public final class CuriosCompat {

    private CuriosCompat() {}

    /** Returns a copy of every curio slot's contents, in slot order. */
    public static List<ItemStack> capture(Player player) {
        List<ItemStack> result = new ArrayList<>();
        CuriosApi.getCuriosInventory(player).resolve().ifPresent(handler -> {
            IItemHandlerModifiable equipped = handler.getEquippedCurios();
            for (int i = 0; i < equipped.getSlots(); i++) {
                result.add(equipped.getStackInSlot(i).m_41777_());
            }
        });
        return result;
    }

    /** Restores previously captured curio slot contents (or clears all slots if stacks is empty/null). */
    public static void apply(Player player, List<ItemStack> stacks) {
        CuriosApi.getCuriosInventory(player).resolve().ifPresent(handler -> {
            IItemHandlerModifiable equipped = handler.getEquippedCurios();
            int slots = equipped.getSlots();
            for (int i = 0; i < slots; i++) {
                ItemStack toSet = (stacks != null && i < stacks.size()) ? stacks.get(i).m_41777_() : ItemStack.f_41583_;
                equipped.setStackInSlot(i, toSet);
            }
        });
    }
}
