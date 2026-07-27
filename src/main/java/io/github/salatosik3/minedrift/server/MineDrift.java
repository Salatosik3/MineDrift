package io.github.salatosik3.minedrift.server;

import io.github.salatosik3.minedrift.server.event.EventBus;
import io.github.salatosik3.minedrift.server.listener.bus.BusEventListenerRegistrar;
import io.github.salatosik3.minedrift.server.listener.fabric.EventListenerRegistrar;
import io.github.salatosik3.minedrift.server.service.ServiceRegistrar;
import io.github.salatosik3.minedrift.server.timer.TimerManager;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MineDrift implements ModInitializer {
	public static final String MOD_ID = "minedrift";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private final EventBus eventBus = new EventBus();
	private final TimerManager timerManager = new TimerManager();
	private final ServiceRegistrar serviceRegistrar = new ServiceRegistrar(timerManager);
	private final EventListenerRegistrar eventListenerRegistrar = new EventListenerRegistrar(eventBus);
	private final BusEventListenerRegistrar busEventListenerRegistrar = new BusEventListenerRegistrar(eventBus, serviceRegistrar);

	@Override
	public void onInitialize() {
		serviceRegistrar.registerAll();
		busEventListenerRegistrar.registerAll();
		eventListenerRegistrar.registerAll();

//		timerManager.cancelAll(); // TODO figure out how to list to server shutdown event to be able to cancel all timers.
	}
}
