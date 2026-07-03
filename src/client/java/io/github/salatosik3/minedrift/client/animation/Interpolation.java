package io.github.salatosik3.minedrift.client.animation;

public class Interpolation {

    private final Type interpolationType;
    private final double minVal;
    private final double maxVal;
    private final long timeToComplete;

    private long lastTime = 0;
    private long accumulatedTime = 0;
    private boolean reversing = false;

    public Interpolation(Type interpolationType, double minVal, double maxVal, long timeToComplete) {
        this.interpolationType = interpolationType;
        this.minVal = minVal;
        this.maxVal = maxVal;
        this.timeToComplete = timeToComplete;
    }

    public Interpolation(Type interpolationType, long timeToComplete) {
        this(interpolationType, 0, 1, timeToComplete);
    }

    public double get() {
        long newTime = System.currentTimeMillis();
        long delay = newTime - (lastTime == 0 ? newTime : lastTime);
        lastTime = newTime;

        double actualTimeToComplete = timeToComplete;
        accumulatedTime += reversing ? -delay : delay;

        if (reversing && accumulatedTime < 0) {
            reversing = false;
            accumulatedTime = 0;
        } else if (!reversing && accumulatedTime > actualTimeToComplete) {
            reversing = true;
        }

        return interpolationType.interpolationFunction.interpolate(minVal, maxVal, (accumulatedTime / actualTimeToComplete));
    }

    public enum Type {
        LINEAR((a, b, t) -> a + (b - a) * t),
        EASE_IN_OUT((a, b, t) -> a - (b - a) * t * t * (3 - 2 * t));

        private final InterpolationFunc interpolationFunction;

        Type(InterpolationFunc interpolationFunction) {
            this.interpolationFunction = interpolationFunction;
        }
    }

    @FunctionalInterface
    public interface InterpolationFunc {
        double interpolate(double a, double b, double t);
    }
}
