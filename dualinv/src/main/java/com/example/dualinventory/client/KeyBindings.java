package com.example.dualinventory.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Registered as a normal vanilla KeyMapping, so the player can freely rebind it from
 * Options > Controls just like any other key - "Z" here is only the default.
 */
public class KeyBindings {

    public static final String CATEGORY = "key.categories.dualinventory";

    public static final KeyMapping SWAP_INVENTORY = new KeyMapping(
            "key.dualinventory.swap",
            InputConstants.Type.KEYSYM,
            90, // GLFW_KEY_Z - a stable GLFW constant, used directly since InputConstants only
                // exposes this as an obfuscated SRG field, not a named "KEY_Z" constant
            CATEGORY
    );

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(SWAP_INVENTORY);
    }
}
