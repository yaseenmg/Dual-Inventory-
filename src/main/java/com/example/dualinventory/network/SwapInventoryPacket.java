package com.example.dualinventory.network;

import com.example.dualinventory.capability.InventorySwapService;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Empty-payload packet: client asks the server to swap this player's active inventory. */
public class SwapInventoryPacket {

    public SwapInventoryPacket() {}

    public SwapInventoryPacket(FriendlyByteBuf buf) {}

    public void encode(FriendlyByteBuf buf) {}

    public void handle(Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                InventorySwapService.swap(player);
            }
        });
        ctx.setPacketHandled(true);
    }
}
