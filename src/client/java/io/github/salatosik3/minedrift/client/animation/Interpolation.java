package io.github.salatosik3.minedrift.client.animation;

public class Interpolation {

    private final Clock clock;
    private Type interpolationType;
    private double minVal;
    private double maxVal;
    private long timeToComplete;
    private boolean reverseOnEnd;

    private long accumulatedTime = 0;
    private boolean reversing = false;

    public Interpolation(Clock clock, Type interpolationType, double minVal, double maxVal, long timeToComplete, boolean reverseOnEnd) {
        this.clock = clock;
        this.interpolationType = interpolationType;
        this.minVal = minVal;
        this.maxVal = maxVal;
        this.timeToComplete = timeToComplete;
        this.reverseOnEnd = reverseOnEnd;
    }

    public Interpolation(Type interpolationType, double minVal, double maxVal, long timeToComplete, boolean reverseOnEnd) {
        this(new Clock(), interpolationType, minVal, maxVal, timeToComplete, reverseOnEnd);
    }

    public double compute() {
        if (!clock.isStarted()) { // Ummm, idk who has responsibility for starting the clock, so I just leave it like this, maybe it will cause problems in the future, maybe not
            clock.start();
        }

        long delay = clock.getDelay();
        accumulatedTime += reversing ? -delay : delay;

        if (accumulatedTime > timeToComplete) {
            if (reverseOnEnd) {
                reversing = true;
                accumulatedTime = timeToComplete;
            } else {
                reversing = false;
                accumulatedTime = 0;
            }
        } else if (accumulatedTime < 0) {
            accumulatedTime = 0;
            reversing = false;
        }

        return interpolationType.func.interpolate(minVal, maxVal, (double) accumulatedTime / timeToComplete);
    }

    public Clock getClock() {
        return clock;
    }

    public Type getInterpolationType() {
        return interpolationType;
    }

    public void setInterpolationType(Type interpolationType) {
        this.interpolationType = interpolationType;
    }

    public double getMinVal() {
        return minVal;
    }

    public void setMinVal(double minVal) {
        this.minVal = minVal;
    }

    public double getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(double maxVal) {
        this.maxVal = maxVal;
    }

    public long getTimeToComplete() {
        return timeToComplete;
    }

    public void setTimeToComplete(long timeToComplete) {
        this.timeToComplete = timeToComplete;
    }

    public void setReverseOnEnd(boolean reverseOnEnd) {
        this.reverseOnEnd = reverseOnEnd;
    }

    public boolean isReverseOnEnd() {
        return reverseOnEnd;
    }

    public enum Type {
        LINEAR((a, b, t) -> a + (b - a) * t),
        EASE_IN_OUT((a, b, t) -> a + (b - a) * t * t * (3 - 2 * t));

        private final InterpolationFunc func;

        Type(InterpolationFunc func) {
            this.func = func;
        }
    }

    @FunctionalInterface
    public interface InterpolationFunc {
        double interpolate(double a, double b, double t);
    }
}
