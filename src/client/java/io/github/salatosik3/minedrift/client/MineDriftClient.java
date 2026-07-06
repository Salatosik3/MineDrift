package io.github.salatosik3.minedrift.client;

import io.github.salatosik3.minedrift.client.animation.Clock;
import io.github.salatosik3.minedrift.client.animation.interpolation.*;
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

	private long lastRenderTime = System.currentTimeMillis();
	private int angleDegrees = 0;
	private Interpolation<Float> interpolation;

	@Override
	public void onInitializeClient() {
		var commonInterpolation = new FloatInterpolation(InterpolationType.LINEAR, 0f, 1f, 30000);
//		commonInterpolation.addOnEndCallback(interpolation -> interpolation.setReverse(!interpolation.isReverse()));
//		interpolation = new RepetitiveInterpolation<>(commonInterpolation, -1);

		interpolation = commonInterpolation;

		HudElementRegistry.addFirst(Identifier.fromNamespaceAndPath(MOD_ID, "trying_to_figure_it_out"),
				(graphics, deltaTracker) -> {

			var pose = graphics.pose();
			pose.pushMatrix();
			renderDriftText(graphics, deltaTracker);
			pose.popMatrix();
		});



	}

	private void renderDriftText(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
		var newTime = System.currentTimeMillis();
		var del = newTime - lastRenderTime;

		if (del < 500) {
//			return;

		} else {
			lastRenderTime = newTime;
		}

		angleDegrees++;
		angleDegrees %= 360;

		float sw = graphics.guiWidth();
		float sh = graphics.guiHeight();
		var pose = graphics.pose();

		float offsetOfBorder = 0.05f; // How far from the border of HUD in percentage
		pose.translate(sw * offsetOfBorder, sh * offsetOfBorder);

		pose.rotate(45);
		float scaleFactor = interpolation.get();
		float maxScaleSize = 2;
		float floorSize = 1;
		pose.scale(maxScaleSize * scaleFactor + floorSize);

		var textGraphics = graphics.textRenderer();
		textGraphics.accept(0, 0, Component.literal(String.valueOf(scaleFactor) + ": " + String.valueOf(interpolation.isEnded())));

//		if (interpolation.isEnded()) {
//			interpolation.restart();
//		}
	}
}