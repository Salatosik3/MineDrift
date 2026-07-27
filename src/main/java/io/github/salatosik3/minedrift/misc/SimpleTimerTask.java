package io.github.salatosik3.minedrift.misc;

public interface SimpleTimerTask {

    void run();

    default java.util.TimerTask asTimerTaskClass() {
        return new java.util.TimerTask() {
            @Override
            public void run() {
                SimpleTimerTask.this.run();
            }
        };
    }
}
