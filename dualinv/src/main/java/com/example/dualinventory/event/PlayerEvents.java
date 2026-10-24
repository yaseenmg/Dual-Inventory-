package com.example.dualinventory.event;

import com.example.dualinventory.capability.CapabilityHandler;
import com.example.dualinventory.capability.DualInventoryData;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/** Copies saved A/B data when Forge replaces a player instance after respawn or travel. */
public class PlayerEvents {
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        DualInventoryData oldData = CapabilityHandler.get(event.getOriginal());
        DualInventoryData newData = CapabilityHandler.get(event.getEntity());
        newData.copyFrom(oldData);
    }
}
