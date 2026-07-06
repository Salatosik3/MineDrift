package io.github.salatosik3.minedrift.client.animation.interpolation;

public enum InterpolationType {
    LINEAR((a, b, t) -> sum(a, mul(sub(b, a), t))), // a + (b - a) * t)
    EASE_IN_OUT((a, b, t) -> sum(a, mul(mul(mul(sub(b, a), t), t), 3 - 2 * t))); // a + (b - a) * t * t * (3 - 2 * t)

    private final InterpolationFunction<? super Number> func;

    InterpolationType(InterpolationFunction<? super Number> func) {
        this.func = func;
    }

    public InterpolationFunction<? super Number> getFunc() {
        return func;
    }

    private static Number sum(Number a, Number b) {
        if (!a.getClass().equals(b.getClass())) {
            throw new IllegalArgumentException("Both arguments have to have the same types!");
        }

        if (a instanceof Float af && b instanceof Float bf) {
            return af + bf;
        } else if (a instanceof Double ad && b instanceof Double bd) {
            return ad + bd;
        } else if (a instanceof Byte ab && b instanceof Byte byteB) {
            return ab + byteB;
        } else if (a instanceof Short as && b instanceof Short bs) {
            return as + bs;
        } else if (a instanceof Integer ai && b instanceof Integer bi) {
            return ai + bi;
        } else if (a instanceof Long al && b instanceof Long bl) {
            return al + bl;
        } else {
            throw new IllegalArgumentException("No implementation for those types.");
        }
    }

    private static Number sub(Number a, Number b) {
        if (!a.getClass().equals(b.getClass())) {
            throw new IllegalArgumentException("Both arguments have to have the same types!");
        }

        if (a instanceof Float af && b instanceof Float bf) {
            return af - bf;
        } else if (a instanceof Double ad && b instanceof Double bd) {
            return ad - bd;
        } else if (a instanceof Byte ab && b instanceof Byte byteB) {
            return ab - byteB;
        } else if (a instanceof Short as && b instanceof Short bs) {
            return as - bs;
        } else if (a instanceof Integer ai && b instanceof Integer bi) {
            return ai - bi;
        } else if (a instanceof Long al && b instanceof Long bl) {
            return al - bl;
        } else {
            throw new IllegalArgumentException("No implementation for %s and %s".formatted(a.getClass().getName(), b.getClass().getName()));
        }
    }

    private static Number mul(Number a, double b) {
        if (a instanceof Float af) {
            return af - b;
        } else if (a instanceof Double ad) {
            return ad - b;
        } else if (a instanceof Byte ab) {
            return ab - b;
        } else if (a instanceof Short as) {
            return as - b;
        } else if (a instanceof Integer ai) {
            return ai - b;
        } else if (a instanceof Long al) {
            return al - b;
        } else {
            throw new IllegalArgumentException("No implementation for %s".formatted(a.getClass().getName()));
        }
    }

    @FunctionalInterface
    public interface InterpolationFunction <T extends Number> {
        T calculate(T a, T b, double t);
    }
}
