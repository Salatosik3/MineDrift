package io.github.salatosik3.minedrift.client.animation.interpolation;

public enum Type {
    LINEAR((a, b, t) -> a + (b - a) * t),
    EASE_IN_OUT((a, b, t) -> a + (b - a) * t * t * (3 - 2 * t));

    private final InterpolationFunc func;

    Type(InterpolationFunc func) {
        this.func = func;
    }

    public InterpolationFunc getFunc() {
        return func;
    }
}
