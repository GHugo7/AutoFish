package mister.autofish;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutofishLostera implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("autofish-lostera");
	public static AutoFishConfig CONFIG;
	public static FishingController CONTROLLER;
	private static KeyBinding toggleKey;

	@Override
	public void onInitializeClient() {
		CONFIG = AutoFishConfig.load();
		CONTROLLER = new FishingController(CONFIG);

		toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.autofish.toggle",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_H,
				"category.autofish"));

		ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
			String text = message.getString();
			if (overlay) LOGGER.info("[actionbar] {}", text);

			if (CONTROLLER.isEnabled() && text.contains(CONFIG.triggerMessage)) {
				CONTROLLER.onBite();
			}
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (toggleKey.wasPressed()) CONTROLLER.toggle(client);
			CONTROLLER.tick(client);
		});

		LOGGER.info("AutoFish initialisé");
	}
}