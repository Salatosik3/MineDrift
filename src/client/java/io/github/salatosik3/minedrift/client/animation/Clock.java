package io.github.salatosik3.minedrift.client.animation;

// TODO clock is good for one source of usage, but bad at multiply, it means that when object A uses clock A while object B also uses clock A, then object A will always get 0 delay. Is this bad? Maybe
// This class was created in mind that an top object creates bottom level objects that rely on this class, so in this way the top level object can in this way synchronize time and control speed from one place.
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
