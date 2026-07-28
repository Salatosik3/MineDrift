package io.github.salatosik3.minedrift.client.motion;

public class Clock {
    private double speed;
    private boolean started = false;
    private long lastTimeValue = 0;

    public Clock(double speed) {
        this.speed = speed;
    }

    public Clock() {
        this(1.0d);
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return this.speed;
    }

    private long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public void start() {
        lastTimeValue = getCurrentTime();
        started = true;
    }

    public void stop() {
        lastTimeValue = 0;
        started = false;
    }

    public boolean isStarted() {
        return this.started;
    }

    // delay since last method call, so it actually returns amount of time that has been passed since first method call (or start method call)
    public long getDelay() {
        if (!started) {
            return 0;
        }
        long currentTime = getCurrentTime();
        long delay = currentTime - lastTimeValue;
        lastTimeValue = currentTime;
        return Math.round((double) delay * this.speed);
    }
}
