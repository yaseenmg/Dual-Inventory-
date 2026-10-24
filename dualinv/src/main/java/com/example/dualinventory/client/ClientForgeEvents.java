package com.example.dualinventory.client;

import com.example.dualinventory.capability.CapabilityHandler;
import com.example.dualinventory.capability.DualInventoryData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientForgeEvents {
    @SubscribeEvent
    public static void onInventoryScreenRender(ScreenEvent.Render.Post event) {
        if (!(event.getScreen() instanceof InventoryScreen screen)) return;
        var player = Minecraft.getInstance().player;
        if (player == null) return;
        DualInventoryData.Slot active = CapabilityHandler.getOptional(player)
                .map(DualInventoryData::getActive).orElse(DualInventoryData.Slot.A);
        String label = "[" + (active == DualInventoryData.Slot.A ? "A" : "B") + "]";
        int x = readIntField(screen, "f_97735_") + 90;
        int y = readIntField(screen, "f_97736_") + 6;
        event.getGuiGraphics().drawString(Minecraft.getInstance().font, label, x, y, 0x55FF55, false);
    }
    private static int readIntField(Object obj, String fieldName) {
        Class<?> cls = obj.getClass();
        while (cls != null) {
            try {
                var field = cls.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.getInt(obj);
            } catch (NoSuchFieldException ignored) {
                cls = cls.getSuperclass();
            } catch (IllegalAccessException e) {
                return 0;
            }
        }
        return 0;
    }
}
