package com.example.dualinventory.client;

import com.example.dualinventory.capability.CapabilityHandler;
import com.example.dualinventory.capability.DualInventoryData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Draws the "[A]"/"[B]" label on the vanilla inventory screen. Registered separately from
 * {@link ClientSetup} because ScreenEvent.Render.Post fires on MinecraftForge.EVENT_BUS
 * (a regular Forge-bus event, NOT IModBusEvent) - bundling it with a mod-bus-only class
 * (like FMLClientSetupEvent's handler) causes Forge to reject the whole class at registration
 * time with an IllegalArgumentException, since it validates every @SubscribeEvent method's
 * parameter type against the specific bus the class is registered on.
 * <p>
 * Vanilla members used here, verified via javap against the real SRG jar:
 *   Minecraft.getInstance() -> m_91087_ ; Minecraft.font (field) -> f_91062_ ; Minecraft.player (field) -> f_91074_
 *   AbstractContainerScreen.leftPos/topPos -> f_97735_ / f_97736_ (made public via accesstransformer.cfg)
 *   GuiGraphics.drawString(Font,String,int,int,int,boolean) -> m_280056_
 */
public class ClientForgeEvents {

    @SubscribeEvent
    public static void onInventoryScreenRender(ScreenEvent.Render.Post event) {
        if (!(event.getScreen() instanceof InventoryScreen screen)) return;

        var player = Minecraft.m_91087_().f_91074_;
        if (player == null) return;

        DualInventoryData.Slot active = CapabilityHandler.getOptional(player)
                .map(DualInventoryData::getActive)
                .orElse(DualInventoryData.Slot.A);

        String label = "[" + (active == DualInventoryData.Slot.A ? "A" : "B") + "]";

        int x = getLeftPos(screen) + 90;
        int y = getTopPos(screen) + 6;

        event.getGuiGraphics().m_280056_(Minecraft.m_91087_().f_91062_, label, x, y, 0x55FF55, false);
    }

    /**
     * Reads the protected leftPos/topPos fields (f_97735_/f_97736_) via reflection instead of
     * direct field access. This mod also ships an accesstransformer.cfg widening these fields
     * to public - reflection here is a defensive fallback that works identically whether or
     * not the AT has taken effect yet.
     */
    private static int getLeftPos(InventoryScreen screen) {
        return readIntFieldUpHierarchy(screen, "f_97735_");
    }

    private static int getTopPos(InventoryScreen screen) {
        return readIntFieldUpHierarchy(screen, "f_97736_");
    }

    private static int readIntFieldUpHierarchy(Object obj, String fieldName) {
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
