package io.github.salatosik3.minedrift.client.animation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class ClockTest {

    private static final int MAXIMUM_ERROR_VALUE = 5;

    Clock clock = new Clock();

    private void riskySleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void resetClock() {
        if (clock.isStarted()) {
            clock.stop();
        }
        clock.setSpeed(1);
    }

    @Test
    @DisplayName("Test default usage")
    public void testObviousUsage() {
        resetClock();

        clock.setSpeed(1);
        clock.start();
        riskySleep(50);
        long delay = clock.getDelay();
        clock.stop();

        long millisError = Math.abs(50 - delay);

        if (millisError > MAXIMUM_ERROR_VALUE) {
            Assertions.fail("Delay error is too high: %s".formatted(millisError));
        }
    }

    @Test
    @DisplayName("Test with 2x speed")
    public void testWithFasterSpeed() {
        resetClock();

        clock.setSpeed(2);
        clock.start();
        riskySleep(100);
        long delay = clock.getDelay();
        clock.stop();

        long millisError = Math.abs(200 - delay);

        if (millisError > MAXIMUM_ERROR_VALUE) {
            Assertions.fail("Delay error is too high: %s".formatted(millisError));
        }
    }

    @Test
    @DisplayName("Test with 0.5x speed")
    public void testWithSlowerSpeed() {
        resetClock();

        clock.setSpeed(0.5);
        clock.start();
        riskySleep(100);
        long delay = clock.getDelay();
        clock.stop();

        long millisError = Math.abs(50 - delay);

        if (millisError > MAXIMUM_ERROR_VALUE) {
            Assertions.fail("Delay error is too high: %s".formatted(millisError));
        }
    }
}
