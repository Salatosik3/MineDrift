package io.github.salatosik3.minedrift;

import io.github.salatosik3.minedrift.event.EventBus;
import io.github.salatosik3.minedrift.listener.bus.BusEventListenerRegistrar;
import io.github.salatosik3.minedrift.listener.fabric.EventListenerRegistrar;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MineDrift implements ModInitializer {
	public static final String MOD_ID = "minedrift";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private final EventBus eventBus = new EventBus();
	private final EventListenerRegistrar eventListenerRegistrar = new EventListenerRegistrar(eventBus);
	private final BusEventListenerRegistrar busEventListenerRegistrar = new BusEventListenerRegistrar(eventBus);

	@Override
	public void onInitialize() {
		busEventListenerRegistrar.registerAll();
		eventListenerRegistrar.registerAll();
	}
}
