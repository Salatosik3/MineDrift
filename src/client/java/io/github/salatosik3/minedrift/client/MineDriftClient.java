package io.github.salatosik3.minedrift.client;

import io.github.salatosik3.minedrift.client.animation.Animation;
import io.github.salatosik3.minedrift.client.animation.Effect;
import io.github.salatosik3.minedrift.client.animation.ShakingEffect;
import io.github.salatosik3.minedrift.client.animation.SlideInAnimation;
import io.github.salatosik3.minedrift.client.animation.interpolation.*;
import io.github.salatosik3.minedrift.client.hud.HudElementRegistrar;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MineDriftClient implements ClientModInitializer {
	public static final String MOD_ID = "minedrift";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private final HudElementRegistrar hudElementRegistrar = new HudElementRegistrar(MOD_ID);

	@Override
	public void onInitializeClient() {
		hudElementRegistrar.registerAll();
	}
//
//	private void renderDriftText(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
//
//		var newTime = System.currentTimeMillis();
//		var del = newTime - lastRenderTime;
//
//		if (anim.isFinished()) {
//			anim.restart();
//		}
//
//		if (del > 10000) {
////			anim.start();
//
//		} else {
//			lastRenderTime = newTime;
//		}
//
//		angleDegrees++;
//		angleDegrees %= 360;
//
//		float sw = graphics.guiWidth();
//		float sh = graphics.guiHeight();
//		var pose = graphics.pose();
//
////		float offsetOfBorder = 0.05f; // How far from the border of HUD in percentage
//		pose.translate(sw / 2, sh / 2);
//
////		pose.rotate(45);
////		float scaleFactor = interpolation.get();
////		float maxScaleSize = 2;
////		float floorSize = 1;
////		pose.scale(maxScaleSize * scaleFactor + floorSize);
//
//		Vec2 animatedVec = shaking.animate();
//		SlideInAnimation.Data slideInData = anim.animate();
//
////		int x = Math.round(animatedVec.x * 10) + Math.round((1 - slideInData.getSlideFactor()) * (sh / 2));
//		int x = Math.round(animatedVec.x * 2);
//		int y = Math.round(animatedVec.y * 2);
//
//		var textGraphics = graphics.textRenderer();
////		textGraphics.defaultParameters(textGraphics.defaultParameters().withOpacity(slideInData.getVisibilityFactor()));
//		textGraphics.accept(x, y, Component.literal("A"));
//
////		if (interpolation.isEnded()) {
////			interpolation.restart();
////		}
//	}
}