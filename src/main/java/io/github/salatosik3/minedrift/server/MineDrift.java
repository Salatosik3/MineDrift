package io.github.salatosik3.minedrift.server;

import io.github.salatosik3.minedrift.server.event.EventBus;
import io.github.salatosik3.minedrift.server.listener.bus.BusEventListenerRegistrar;
import io.github.salatosik3.minedrift.server.listener.fabric.EventListenerRegistrar;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MineDrift implements ModInitializer {
	public static final String MOD_ID = "minedrift";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private final EventBus eventBus = new EventBus();

	// TODO Something wrong with these... (namings)
	private final EventListenerRegistrar eventListenerRegistrar = new EventListenerRegistrar(eventBus);
	private final BusEventListenerRegistrar busEventListenerRegistrar = new BusEventListenerRegistrar(eventBus);

	@Override
	public void onInitialize() {
		busEventListenerRegistrar.registerAll();
		eventListenerRegistrar.registerAll();
	}
}
