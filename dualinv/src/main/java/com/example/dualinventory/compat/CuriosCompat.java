package com.example.dualinventory.compat;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.List;

/** Optional integration with Curios; saves and restores all equipped Curios slots. */
public final class CuriosCompat {
    private CuriosCompat() {}

    public static List<ItemStack> capture(Player player) {
        List<ItemStack> result = new ArrayList<>();
        CuriosApi.getCuriosInventory(player).resolve().ifPresent(handler -> {
            IItemHandlerModifiable equipped = handler.getEquippedCurios();
            for (int i = 0; i < equipped.getSlots(); i++) result.add(equipped.getStackInSlot(i).m_41777_());
        });
        return result;
    }

    public static void apply(Player player, List<ItemStack> stacks) {
        CuriosApi.getCuriosInventory(player).resolve().ifPresent(handler -> {
            IItemHandlerModifiable equipped = handler.getEquippedCurios();
            int slots = equipped.getSlots();
            for (int i = 0; i < slots; i++) {
                ItemStack toSet = (stacks != null && i < stacks.size())
                        ? stacks.get(i).m_41777_() : ItemStack.f_41583_;
                equipped.setStackInSlot(i, toSet);
            }
        });
    }
}
