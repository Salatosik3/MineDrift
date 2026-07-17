package io.github.salatosik3.minedrift.client.hud;

import io.github.salatosik3.minedrift.client.hud.elements.DriftPointCounter;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class CustomHudRegister {
    private static final Map<Class<?>, ? super HudElement> huds = new HashMap<>();

    private CustomHudRegister() {
        // I hate this design
    }

    static {
        register(DriftPointCounter::new, DriftPointCounter.ID);
    }

    private static <T extends HudElement> void register(Supplier<T> constructor, Identifier identifier) {
        T instance = constructor.get();
        HudElementRegistry.addLast(identifier, instance);
        huds.put(instance.getClass(), instance);
    }

    @SuppressWarnings("unchecked")
    public static <T extends HudElement> T get(Class<T> clazz) {
        Object hud = huds.get(clazz);
        if (hud == null) {
            throw new RuntimeException("No hud was found for this class.");
        }
        return (T) hud;
    }
}
