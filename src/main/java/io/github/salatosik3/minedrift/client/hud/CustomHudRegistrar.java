package io.github.salatosik3.minedrift.client.hud;

import io.github.salatosik3.minedrift.client.handler.packet.DriftDataSource;
import io.github.salatosik3.minedrift.client.hud.elements.DriftPointCounter;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class CustomHudRegistrar {
    private static final Map<Class<?>, ? super HudElement> huds = new HashMap<>();

    private CustomHudRegistrar() {
        // I hate this design
    }

    public static void registerAll() {
        register(() -> {
            return new DriftPointCounter(new DriftDataSource() {
                @Override
                public int getPoints() {
                    return 0;
                }
            });
        }, DriftPointCounter.ID);
    }

    // TODO I should make some general generic class for this purpose, like Registrar<T> and it will have get, register and so on
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
