package io.github.salatosik3.minedrift.client.hud;

import io.github.salatosik3.minedrift.client.hud.elements.DriftPointCounter;
import io.github.salatosik3.minedrift.misc.Registrar;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public final class CustomHudRegistrar {
    private static final Registrar registrar = new Registrar();

    private CustomHudRegistrar() {
        // I hate this design
    }

    public static void registerAll() {
        register(DriftPointCounter::new, DriftPointCounter.ID);
    }

    public static <T extends HudElement> void register(Supplier<T> supplier, Identifier identifier) {
        HudElement instance = supplier.get();
        HudElementRegistry.addLast(identifier, instance);
        registrar.register(instance.getClass(), instance);
    }

    public static <T extends HudElement> T get(Class<T> clazz) {
        return registrar.get(clazz);
    }
}
