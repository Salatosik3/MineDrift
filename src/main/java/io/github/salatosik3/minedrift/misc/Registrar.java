package io.github.salatosik3.minedrift.misc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Registrar {
    private final Map<Class<?>, Object> objects = new HashMap<>();

    public void register(Class<?> clazz, Object object) {
        objects.put(clazz, object);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> clazz) {
        Object object = objects.get(clazz);
        if (object == null) {
            throw new IllegalStateException("No object for %s".formatted(clazz.getName()));
        }
        return (T) object;
    }
}
