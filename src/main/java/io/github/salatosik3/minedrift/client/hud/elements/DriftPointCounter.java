package io.github.salatosik3.minedrift.client.hud.elements;

import io.github.salatosik3.minedrift.client.MineDriftClient;
import io.github.salatosik3.minedrift.client.motion.animation.SlideInAnimation;
import io.github.salatosik3.minedrift.client.motion.effect.ShakingEffect;
import io.github.salatosik3.minedrift.client.motion.interpolation.Interpolation;
import io.github.salatosik3.minedrift.client.motion.interpolation.LinearInterpolation;
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

    private final Interpolation scoreInterpolation = new LinearInterpolation(200);
    private final ShakingEffect shakingEffect = new ShakingEffect();
    private final SlideInAnimation slideAnimation = new SlideInAnimation(500);

    private DriftState driftState = null;
    private float oldScore = 0;
    private float newScore = 0;

    private float lastTextVisibility = 0f;

    public DriftPointCounter() {
        // TODO it isn't good in my opinion, so I have to change everything later
        PacketHandlerRegistrar.register(this::onStateChange, DriftStatePayload.TYPE);
        PacketHandlerRegistrar.register(this::onDrift, DriftPayload.TYPE);
    }

    private void onStateChange(DriftStatePayload payload, ClientPlayNetworking.Context context) {
        driftState = payload.state();
    }

    private void onDrift(DriftPayload payload, ClientPlayNetworking.Context context) {
        this.oldScore = (float) payload.oldScore();
        this.newScore = (float) payload.newScore();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        var matrices = graphics.pose();
        matrices.pushMatrix();
        matrices.translate((float) graphics.guiWidth() / 2, (float) graphics.guiHeight() / 5);

        int interpolatedScoreValue = (int) Math.floor(oldScore + scoreInterpolation.interpolate() * newScore);
        float x = 0, y = 0;
        float maxCoordinateOffset = 2;

        var shakingVec = shakingEffect.animate();
        x += (maxCoordinateOffset * shakingVec.x) - maxCoordinateOffset / 2;
        y += (maxCoordinateOffset * shakingVec.y) - maxCoordinateOffset / 2;

        var textRenderer = graphics.textRenderer();

        if (driftState != null) {
            var slideAnimData = slideAnimation.animate();

            boolean reverse = switch (driftState) {
                case STARTED -> false;
                case ENDED, FAILED -> true;
            };
            slideAnimation.setReverse(reverse);

            float slideOffset = 20 * slideAnimData.getSlideFactor();
            MineDriftClient.LOGGER.debug("SlideFactor: " + slideAnimData.getSlideFactor());

            y += slideOffset;
            lastTextVisibility = slideAnimData.getVisibilityFactor();

            if (slideAnimation.isFinished()) {
                driftState = null;
                slideAnimation.reset();
            }
        }

        textRenderer.defaultParameters(textRenderer.defaultParameters().withOpacity(lastTextVisibility));
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
