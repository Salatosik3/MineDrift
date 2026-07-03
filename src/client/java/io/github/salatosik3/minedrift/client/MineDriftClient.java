package io.github.salatosik3.minedrift.client;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import io.github.salatosik3.minedrift.client.animation.Interpolation;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MineDriftClient implements ClientModInitializer {
	public static final String MOD_ID = "minedrift";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private long lastRenderTime = 0;
	private int angleDegrees = 0;
	private Interpolation interpolation = new Interpolation(Interpolation.Type.EASE_IN_OUT, 1000);

	@Override
	public void onInitializeClient() {

		HudElementRegistry.addFirst(Identifier.fromNamespaceAndPath(MOD_ID, "trying_to_figure_it_out"),
				(graphics, deltaTracker) -> {

			var pose = graphics.pose();
			pose.pushMatrix();
			renderDriftText(graphics, deltaTracker);
			pose.popMatrix();
		});

	}

	private void renderDriftText(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
		var newRenderTime = System.currentTimeMillis();
		var renderDelay = newRenderTime - lastRenderTime;
		lastRenderTime = newRenderTime;



//		if (renderDelay <= 50) {
//			return;
//		}

		angleDegrees++;
		angleDegrees %= 360;

		float sw = graphics.guiWidth();
		float sh = graphics.guiHeight();
		var pose = graphics.pose();

		float offsetOfBorder = 0.05f; // How far from the border of HUD in percentage
		pose.translate(sw * offsetOfBorder, sh * offsetOfBorder);

		pose.rotate(45);
//		float scaleFactor = (float) Math.abs(Math.cos(Math.toRadians(angleDegrees) * 1));
		float scaleFactor = (float) interpolation.get();
		float maxScaleSize = 1;
		float floorSize = 2;
		pose.scale(maxScaleSize * scaleFactor + floorSize);

		var textGraphics = graphics.textRenderer();
		textGraphics.accept(0, 0, Component.literal("Test!"));
	}
}