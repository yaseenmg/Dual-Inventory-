package com.example.dualinventory.client;

import com.example.dualinventory.network.NetworkHandler;
import com.example.dualinventory.network.SwapInventoryPacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientEvents {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (Minecraft.m_91087_().f_91074_ == null) return;
        while (KeyBindings.SWAP_INVENTORY.m_90859_()) {
            NetworkHandler.CHANNEL.sendToServer(new SwapInventoryPacket());
        }
    }
}
