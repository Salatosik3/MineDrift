package io.github.salatosik3.minedrift.client.animation;

import io.github.salatosik3.minedrift.client.animation.interpolation.FloatEaseInOutInterpolation;
import io.github.salatosik3.minedrift.client.animation.interpolation.FloatLinearInterpolation;
import io.github.salatosik3.minedrift.client.animation.interpolation.Interpolation;

public class SlideInAnimation implements Animation<SlideInAnimation.Data> {

    private final Interpolation<Float> slideInterpolation, visibilityInterpolation;
    private final Data data = new Data();

    public SlideInAnimation(long slideDuration, long visibilityDuration) {
        this.slideInterpolation = new FloatEaseInOutInterpolation(0.0f, 1.0f, slideDuration);
        slideInterpolation.setReverse(true);
        this.visibilityInterpolation = new FloatLinearInterpolation(0.0f, 1.0f, visibilityDuration);
    }

    public SlideInAnimation(long duration) {
        this(duration, duration);
    }

    @Override
    public Data animate() {
        data.slideFactor = slideInterpolation.get();
        data.visibilityFactor = visibilityInterpolation.get();
        return data;
    }

    @Override
    public boolean isFinished() {
        return slideInterpolation.isFinished() && visibilityInterpolation.isFinished();
    }

    @Override
    public boolean isStopped() {
        return slideInterpolation.isStopped() && visibilityInterpolation.isStopped();
    }

    @Override
    public void start() {
        slideInterpolation.start();
        visibilityInterpolation.start();
    }

    @Override
    public void stop() {
        slideInterpolation.stop();
        visibilityInterpolation.stop();
    }

    @Override
    public void restart() {
        slideInterpolation.restart();
        visibilityInterpolation.restart();
    }

    @Override
    public void reset() {
        slideInterpolation.reset();
        visibilityInterpolation.reset();
    }

    public static class Data {
        private float slideFactor;
        private float visibilityFactor;

        public float getSlideFactor() {
            return this.slideFactor;
        }

        public float getVisibilityFactor() {
            return this.visibilityFactor;
        }
    }
}
