package io.github.salatosik3.minedrift.client.animation;

public class Interpolation {

    private final Clock clock;
    private Type interpolationType;
    private double minVal;
    private double maxVal;
    private long timeToComplete;
    private Mode mode;

    private long accumulatedTime = 0;
    private boolean reversing = false;
    private boolean stopped = false;

    public Interpolation(Clock clock, Type interpolationType, double minVal, double maxVal, long timeToComplete, Mode mode) {
        this.clock = clock;
        this.interpolationType = interpolationType;
        this.minVal = minVal;
        this.maxVal = maxVal;
        this.timeToComplete = timeToComplete;
        this.mode = mode;
    }

    public Interpolation(Type interpolationType, double minVal, double maxVal, long timeToComplete, Mode mode) {
        this(new Clock(), interpolationType, minVal, maxVal, timeToComplete, mode);
    }

    public double compute() {
        if (stopped) {
            return interpolationType.func.interpolate(minVal, maxVal, (double) accumulatedTime / timeToComplete);
        }

        long delay = clock.getDelay();
        accumulatedTime += reversing ? -delay : delay;

        if (accumulatedTime > timeToComplete) {
            switch (mode) {
                case REVERSING_CYCLE -> {
                    reversing = true;
                    accumulatedTime = timeToComplete;
                }
                case CYCLE -> {
                    reversing = false;
                    accumulatedTime = 0;
                }
                case STOP_WHEN_END -> {
                    stopped = true;
                    accumulatedTime = timeToComplete;
                }
            }
        } else if (accumulatedTime < 0) {
            accumulatedTime = 0;
            reversing = false;
        }

        return interpolationType.func.interpolate(minVal, maxVal, (double) accumulatedTime / timeToComplete);
    }

    public void stop() {
        this.stopped = true;
    }

    public void reset() {
        accumulatedTime = 0;
        stopped = false;
        reversing = false;
    }

    public Clock getClock() {
        return clock;
    }

    public Type getInterpolationType() {
        return interpolationType;
    }

    public double getMinVal() {
        return minVal;
    }

    public double getMaxVal() {
        return maxVal;
    }

    public long getTimeToComplete() {
        return timeToComplete;
    }

    public Mode getMode() {
        return mode;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setInterpolationType(Type interpolationType) {
        this.interpolationType = interpolationType;
    }

    public void setMinVal(double minVal) {
        this.minVal = minVal;
    }

    public void setMaxVal(double maxVal) {
        this.maxVal = maxVal;
    }

    public void setTimeToComplete(long timeToComplete) {
        this.timeToComplete = timeToComplete;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
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

    public enum Mode {
        STOP_WHEN_END, CYCLE, REVERSING_CYCLE
    }
}
