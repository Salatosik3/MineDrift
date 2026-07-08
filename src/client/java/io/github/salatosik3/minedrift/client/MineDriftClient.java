package io.github.salatosik3.minedrift.client;

import io.github.salatosik3.minedrift.client.animation.Animation;
import io.github.salatosik3.minedrift.client.animation.Effect;
import io.github.salatosik3.minedrift.client.animation.ShakingEffect;
import io.github.salatosik3.minedrift.client.animation.SlideInAnimation;
import io.github.salatosik3.minedrift.client.animation.interpolation.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MineDriftClient implements ClientModInitializer {
	public static final String MOD_ID = "minedrift";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private long lastRenderTime = System.currentTimeMillis();
	private int angleDegrees = 0;
	private Interpolation<Float> interpolation;
	private Effect<Vec2> shaking = new ShakingEffect();
	private Animation<SlideInAnimation.Data> anim = new SlideInAnimation(5000);

	@Override
	public void onInitializeClient() {
//		interpolation= new FloatInterpolation(InterpolationType.EASE_IN_OUT, 0f, 1f, 2000);
//		interpolation.addOnEndCallback(interpolation -> {
//			interpolation.setReverse(!interpolation.isReverse());
//			interpolation.restart();
//		});

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

		if (anim.isFinished()) {
			anim.restart();
		}

		if (del > 10000) {
//			anim.start();

		} else {
			lastRenderTime = newTime;
		}

		angleDegrees++;
		angleDegrees %= 360;

		float sw = graphics.guiWidth();
		float sh = graphics.guiHeight();
		var pose = graphics.pose();

		float offsetOfBorder = 0.05f; // How far from the border of HUD in percentage
		pose.translate(sw / 2, sh * offsetOfBorder);

//		pose.rotate(45);
//		float scaleFactor = interpolation.get();
//		float maxScaleSize = 2;
//		float floorSize = 1;
//		pose.scale(maxScaleSize * scaleFactor + floorSize);

		Vec2 animatedVec = shaking.animate();
		SlideInAnimation.Data slideInData = anim.animate();

		int x = Math.round(animatedVec.x * 10) + Math.round((1 - slideInData.getSlideFactor()) * (sh / 2));
		int y = Math.round(animatedVec.y * 10);

		var textGraphics = graphics.textRenderer();
		textGraphics.defaultParameters(textGraphics.defaultParameters().withOpacity(slideInData.getVisibilityFactor()));
		textGraphics.accept(x, y, Component.literal("A"));

//		if (interpolation.isEnded()) {
//			interpolation.restart();
//		}
	}
}