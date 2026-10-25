package com.example.dualinventory.event;

import com.example.dualinventory.capability.CapabilityHandler;
import com.example.dualinventory.capability.DualInventoryData;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Forge replaces the Player object on respawn/dimension travel; capabilities are NOT
 * copied automatically unless explicitly handled here. Regular logout/login within the
 * same world already persists correctly on its own (capabilities save to the player's
 * NBT on disk), so this only covers the in-session "new Player instance" case.
 */
public class PlayerEvents {

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        // Death does not clear inventories in this mod (they are stored independently of the
        // live vanilla inventory), so the saved slot data must always carry over regardless
        // of whether this clone is from death or a dimension change.
        DualInventoryData oldData = CapabilityHandler.get(event.getOriginal());
        DualInventoryData newData = CapabilityHandler.get(event.getEntity());
        newData.copyFrom(oldData);
    }
}
