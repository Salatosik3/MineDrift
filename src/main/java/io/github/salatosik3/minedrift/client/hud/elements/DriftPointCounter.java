package io.github.salatosik3.minedrift.client.hud.elements;

import io.github.salatosik3.minedrift.client.MineDriftClient;
import io.github.salatosik3.minedrift.client.animation.ShakingEffect;
import io.github.salatosik3.minedrift.client.animation.SlideInAnimation;
import io.github.salatosik3.minedrift.client.animation.interpolation.FloatLinearInterpolation;
import io.github.salatosik3.minedrift.client.packet.PacketHandlerRegistrar;
import io.github.salatosik3.minedrift.networking.client.DriftPayload;
import io.github.salatosik3.minedrift.networking.client.DriftState;
import io.github.salatosik3.minedrift.networking.client.DriftStatePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class DriftPointCounter implements HudElement {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(MineDriftClient.MOD_ID, "drift_point_counter");

    private final FloatLinearInterpolation scoreInterpolation = new FloatLinearInterpolation(0f, 0f, 200);
    private final ShakingEffect shakingEffect = new ShakingEffect();
    private final SlideInAnimation slideAnimation = new SlideInAnimation(2000);

    private DriftState driftState = null;

    public DriftPointCounter() {
        // TODO it isn't good in my opinion, so I have to change everything later
        PacketHandlerRegistrar.register(this::onStateChange, DriftStatePayload.TYPE);
        PacketHandlerRegistrar.register(this::onDrift, DriftPayload.TYPE);
    }

    private void onStateChange(DriftStatePayload payload, ClientPlayNetworking.Context context) {
        driftState = payload.state();
    }

    private void onDrift(DriftPayload payload, ClientPlayNetworking.Context context) {
        scoreInterpolation.setMin((float) payload.oldScore());
        scoreInterpolation.setMax((float) payload.newScore());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        var matrices = graphics.pose();
        matrices.pushMatrix();
        matrices.translate((float) graphics.guiWidth() / 2, (float) graphics.guiHeight() / 5);

        int interpolatedScoreValue = (int) Math.floor(scoreInterpolation.get());
        float x = 0, y = 0;
        float maxCoordinateOffset = 4;

        var shakingVec = shakingEffect.animate();
        x += (maxCoordinateOffset * shakingVec.x) - maxCoordinateOffset / 2;
        y += (maxCoordinateOffset * shakingVec.y) - maxCoordinateOffset / 2;

        var textRenderer = graphics.textRenderer();

        if (driftState != null) {
            var slideAnimData = slideAnimation.animate();

            float slideFactor = slideAnimData.getSlideFactor();
            float visibilityFactor = slideAnimData.getVisibilityFactor();


            boolean reverseAnimValue = switch (driftState) {
                case STARTED -> false;
                case ENDED, FAILED -> true;
            };

            if (reverseAnimValue) {
                slideFactor = 1.0f - slideFactor;
                visibilityFactor = 1.0f - visibilityFactor;
            }

            float maxSlideOffset = 10;
            float slideOffset = maxSlideOffset * slideFactor;

            MineDriftClient.LOGGER.debug("slide factor: %s, visibility facto: %s".formatted(slideFactor, visibilityFactor));
            x += slideOffset;
            textRenderer.defaultParameters(textRenderer.defaultParameters().withOpacity(visibilityFactor));

            if (slideAnimation.isFinished()) {
                driftState = null;
                slideAnimation.reset();
            }
        }

        textRenderer.accept(TextAlignment.CENTER, Math.round(x), Math.round(y), Component.literal(String.valueOf(interpolatedScoreValue)));
        matrices.popMatrix();
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
