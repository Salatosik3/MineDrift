package io.github.salatosik3.testing;

import io.github.salatosik3.testing.event.EventBus;
import io.github.salatosik3.testing.listener.bus.BusEventListenerManager;
import io.github.salatosik3.testing.listener.fabric.EventListenerManager;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// TODO I think it will be so laggy if I try to send packets continuosly until a player is drifting, so I will have to make third module in the project that connects two sides (it is needed because of code organization, I want things to be organized)
public class AmogusTesting implements ModInitializer {
	public static final String MOD_ID = "amogustesting";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private final EventBus eventBus = new EventBus();
	private final EventListenerManager eventListenerManager = new EventListenerManager(eventBus);
	private final BusEventListenerManager busEventListenerManager = new BusEventListenerManager(eventBus);

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		busEventListenerManager.registerAll();
		eventListenerManager.registerAll();
	}
}
