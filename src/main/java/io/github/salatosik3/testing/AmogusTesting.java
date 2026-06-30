package io.github.salatosik3.testing;

import io.github.salatosik3.testing.event.EventBus;
import io.github.salatosik3.testing.listener.bus.BusEventListenerManager;
import io.github.salatosik3.testing.listener.fabric.EventListenerManager;
import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
