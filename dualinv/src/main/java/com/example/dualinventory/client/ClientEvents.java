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
        if (Minecraft.m_91087_().f_91074_ == null) return; // m_91087_ = getInstance(); f_91074_ = player field

        while (KeyBindings.SWAP_INVENTORY.m_90859_()) { // m_90859_ = consumeClick()
            NetworkHandler.CHANNEL.sendToServer(new SwapInventoryPacket());
        }
    }
}
