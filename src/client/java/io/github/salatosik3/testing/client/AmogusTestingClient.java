package io.github.salatosik3.testing.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AmogusTestingClient implements ClientModInitializer {
	public static final String MOD_ID = "amogustesting";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	@Override
	public void onInitializeClient() {
		HudElementRegistry.addFirst(Identifier.fromNamespaceAndPath(MOD_ID, "trying_to_figure_it_out"),
				(graphics, deltaTracker) -> {
			graphics.textRenderer().accept(graphics.guiWidth() / 2, graphics.guiHeight() / 2, Component.literal("Go fuck yourself."));
		});

	}
}