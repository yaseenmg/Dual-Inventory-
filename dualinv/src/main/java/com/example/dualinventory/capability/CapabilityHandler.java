package com.example.dualinventory.capability;

import com.example.dualinventory.DualInventoryMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.Optional;

public class CapabilityHandler {

    public static Capability<DualInventoryData> DUAL_INVENTORY =
            CapabilityManager.get(new CapabilityToken<>() {});

    private static final ResourceLocation ID = new ResourceLocation(DualInventoryMod.MOD_ID, "dual_inventory");

    public static void register(IEventBus modBus) {
        modBus.addListener(CapabilityHandler::onCommonSetup);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new Registrar());
    }

    private static void onCommonSetup(FMLCommonSetupEvent event) {
        // capability token is created eagerly above via CapabilityManager.get(); nothing else needed here.
    }

    /** Convenience accessor with a safe empty-capability fallback. */
    public static DualInventoryData get(Player player) {
        return player.getCapability(DUAL_INVENTORY).orElseGet(DualInventoryData::new);
    }

    public static Optional<DualInventoryData> getOptional(Player player) {
        return player.getCapability(DUAL_INVENTORY).resolve();
    }

    /** Separate small class so we can register it on the Forge event bus for AttachCapabilitiesEvent. */
    public static class Registrar {
        @SubscribeEvent
        public void onAttachCapabilities(AttachCapabilitiesEvent<net.minecraft.world.entity.Entity> event) {
            if (event.getObject() instanceof Player) {
                event.addCapability(ID, new DualInventoryProvider());
            }
        }
    }
}
