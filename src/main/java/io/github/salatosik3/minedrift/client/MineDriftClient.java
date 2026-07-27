package io.github.salatosik3.minedrift.client;
import io.github.salatosik3.minedrift.client.hud.CustomHudRegistrar;
import io.github.salatosik3.minedrift.networking.CustomPayloadRegistrar;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MineDriftClient implements ClientModInitializer {
	public static final String MOD_ID = "minedrift";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		CustomPayloadRegistrar.registerAll();
		CustomHudRegistrar.registerAll();
	}
}