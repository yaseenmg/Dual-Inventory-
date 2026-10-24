package com.example.dualinventory;

import com.example.dualinventory.capability.CapabilityHandler;
import com.example.dualinventory.client.ClientSetup;
import com.example.dualinventory.client.KeyBindings;
import com.example.dualinventory.event.PlayerEvents;
import com.example.dualinventory.network.NetworkHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DualInventoryMod.MOD_ID)
public class DualInventoryMod {
    public static final String MOD_ID = "dualinventory";
    public DualInventoryMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        CapabilityHandler.register(modBus);
        NetworkHandler.register();
        MinecraftForge.EVENT_BUS.register(PlayerEvents.class);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            modBus.register(ClientSetup.class);
            modBus.register(KeyBindings.class);
            MinecraftForge.EVENT_BUS.register(com.example.dualinventory.client.ClientEvents.class);
            MinecraftForge.EVENT_BUS.register(com.example.dualinventory.client.ClientForgeEvents.class);
        });
    }
}
