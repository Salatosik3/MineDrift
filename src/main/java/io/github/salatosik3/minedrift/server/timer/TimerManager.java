package io.github.salatosik3.minedrift.server.timer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class TimerManager implements TimerProvider {

    private final List<Timer> timers = new ArrayList<>();

    @Override
    public Timer getTimer() {
        var timer = new Timer();
        timers.add(timer);
        return timer;
    }

    public void cancelAll() {
        timers.forEach(Timer::cancel);
    }
}
