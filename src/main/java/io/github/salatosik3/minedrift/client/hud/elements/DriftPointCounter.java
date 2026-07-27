package io.github.salatosik3.minedrift.client.hud.elements;

import io.github.salatosik3.minedrift.client.MineDriftClient;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

public class DriftPointCounter implements HudElement {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(MineDriftClient.MOD_ID, "drift_point_counter");

    public DriftPointCounter() {

    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        // TODO render
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
