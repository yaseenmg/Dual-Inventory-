package com.example.dualinventory.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KeyBindings {
    public static final String CATEGORY = "key.categories.dualinventory";
    public static final KeyMapping SWAP_INVENTORY = new KeyMapping(
            "key.dualinventory.swap", InputConstants.Type.KEYSYM, 90, CATEGORY);
    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(SWAP_INVENTORY);
    }
}
