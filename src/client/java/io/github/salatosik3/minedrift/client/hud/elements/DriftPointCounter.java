package io.github.salatosik3.minedrift.client.hud.elements;

import io.github.salatosik3.minedrift.client.MineDriftClient;
import io.github.salatosik3.minedrift.client.handler.packet.DriftDataSource;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

public class DriftPointCounter implements HudElement {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(MineDriftClient.MOD_ID, "drift_point_counter");

    private final DriftDataSource driftDataSource;

    public DriftPointCounter(DriftDataSource driftDataSource) {
        this.driftDataSource = driftDataSource;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        // TODO render
    }
}
