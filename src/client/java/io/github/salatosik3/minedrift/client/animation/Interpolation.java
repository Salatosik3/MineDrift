package io.github.salatosik3.minedrift.client.animation;

public class Interpolation {
    private final double minVal;
    private final double maxVal;
    private final long timeToComplete;

    private long lastTime = 0;
    private long accumulatedTime = 0;
    private boolean reversing = false;

    public Interpolation(double minVal, double maxVal, long timeToComplete) {
        this.minVal = minVal;
        this.maxVal = maxVal;
        this.timeToComplete = timeToComplete;
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

        return minVal + (maxVal - minVal) * (accumulatedTime / actualTimeToComplete);
    }
}
