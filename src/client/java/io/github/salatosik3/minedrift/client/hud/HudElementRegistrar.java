package io.github.salatosik3.minedrift.client.hud;

import io.github.salatosik3.minedrift.client.hud.elements.DriftPointCounter;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.resources.Identifier;

public class HudElementRegistrar {

    private final String modId;

    public HudElementRegistrar(String modId) {
        this.modId = modId;
    }

    private void register(String path, HudElement hudElement) {
        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath(modId, path), hudElement);
    }

    public void registerAll() {
        register("drift_point_counter", new DriftPointCounter());
    }
}
