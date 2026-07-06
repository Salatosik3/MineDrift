package io.github.salatosik3.minedrift.client.animation.interpolation;

// Weirdest thing that I have ever did. TODO I have to delete this shit because it gonna be so tired when I will try to add more complicated things...
public enum InterpolationType {
    LINEAR((a, b, t) -> sum(a, mulByT(sub(b, a), t))), // a + (t - a) * t)
    EASE_IN_OUT((a, b, t) -> sum(a, mulByT(mulByT(mulByT(sub(b, a), t), t), 3 - 2 * t))); // a + (t - a) * t * t * (3 - 2 * t)

    private final InterpolationFunction<? super Number> func;

    InterpolationType(InterpolationFunction<? super Number> func) {
        this.func = func;
    }

    public InterpolationFunction<? super Number> getFunc() {
        return func;
    }

    private static Number sum(Number a, Number b) {
        if (!a.getClass().equals(b.getClass())) {
            throw new IllegalArgumentException("Both arguments have to have the same types! Classes: %s and %s".formatted(a.getClass().getName(), b.getClass().getName()));
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

    private static Number mulByT(Number a, double t) {
        if (a instanceof Float af) {
            return (float) (af * t);
        } else if (a instanceof Double ad) {
            return ad * t;
        } else if (a instanceof Integer ai) {
            return ai * t;
        } else if (a instanceof Long al) {
            return al * t;
        } else {
            throw new IllegalArgumentException("%s is unsupported".formatted(a.getClass().getName()));
        }
    }

    @FunctionalInterface
    public interface InterpolationFunction <T extends Number> {
        T calculate(T a, T b, double t);
    }
}
