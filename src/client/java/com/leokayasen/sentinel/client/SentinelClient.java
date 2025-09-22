package com.leokayasen.sentinel.client;

import com.leokayasen.sentinel.client.gui.AdminPanelScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class SentinelClient implements ClientModInitializer {
    private static KeyBinding openAdminPanelKey;

    @Override
    public void onInitializeClient() {
        // Register keybind for opening admin panel
        openAdminPanelKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sentinel.open_admin_panel",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F8, // F8 key
                "category.sentinel.admin"
        ));

        // Register client tick event to handle keybind
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openAdminPanelKey.wasPressed()) {
                if (client.player != null && client.player.hasPermissionLevel(2)) { // OP level 2 or higher
                    client.setScreen(new AdminPanelScreen());
                }
            }
        });
    }
}