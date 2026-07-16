package io.github.salatosik3.minedrift;

import io.github.salatosik3.minedrift.event.EventBus;
import io.github.salatosik3.minedrift.listener.bus.BusEventListenerManager;
import io.github.salatosik3.minedrift.listener.fabric.EventListenerManager;
import io.github.salatosik3.minedrift.networking.Networking;
import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// TODO I think it will be so laggy if I try to send packets continuosly until a player is drifting, so I will have to make third module in the project that connects two sides (it is needed because of code organization, I want things to be organized)
public class MineDrift implements ModInitializer {
	public static final String MOD_ID = "minedrift";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private final EventBus eventBus = new EventBus();
	private final EventListenerManager eventListenerManager = new EventListenerManager(eventBus);
	private final BusEventListenerManager busEventListenerManager = new BusEventListenerManager(eventBus);

	@Override
	public void onInitialize() {
		busEventListenerManager.registerAll();
		eventListenerManager.registerAll();
	}
}
