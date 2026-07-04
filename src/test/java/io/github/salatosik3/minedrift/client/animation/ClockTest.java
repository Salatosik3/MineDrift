package io.github.salatosik3.minedrift.client.animation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class ClockTest {

    Clock clock = new Clock();

    private void riskySleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Test
    @DisplayName("Test default usage")
    public void testObviousUsage() {
        clock.setSpeed(1);
        clock.start();
        riskySleep(50);
        long delay = clock.getDelay();
        clock.stop();

        long millisError = Math.abs(50 - delay);

        if (millisError > 5) {
            Assertions.fail("Delay error is too high: %s".formatted(millisError));
        }
    }

    @Test
    @DisplayName("Test with another speed value")
    public void testWithAnotherSpeed() {
        clock.setSpeed(2);
        clock.start();
        riskySleep(100);
        long delay = clock.getDelay();
        clock.stop();

        long millisError = Math.abs(200 - delay);

        if (millisError > 5) {
            Assertions.fail("Delay error is too high: %s".formatted(millisError));
        }
    }
}
