package io.github.salatosik3.minedrift.client.motion.animation;

import io.github.salatosik3.minedrift.client.motion.interpolation.EaseInOutInterpolation;
import io.github.salatosik3.minedrift.client.motion.interpolation.Interpolation;
import io.github.salatosik3.minedrift.client.motion.interpolation.LinearInterpolation;

public class SlideInAnimation implements Animation<SlideInAnimationData> {

    private final Interpolation slideInt;
    private final Interpolation visibilityInt;

    private final SlideInAnimationData data = new SlideInAnimationData();

    public SlideInAnimation(long slideDuration, long visibilityDuration) {
        slideInt = new EaseInOutInterpolation(slideDuration);
        visibilityInt = new LinearInterpolation(visibilityDuration);
    }

    public SlideInAnimation(long duration) {
        this(duration, duration);
    }

    public SlideInAnimation() {
        this(1000L);
    }

    @Override
    public SlideInAnimationData animate() {
        data.slideFactor = slideInt.interpolate();
        data.visibilityFactor = visibilityInt.interpolate();
        return data;
    }

    @Override
    public void setReverse(boolean reverse) {
        slideInt.setReverse(reverse);
        visibilityInt.setReverse(reverse);
    }

    @Override
    public boolean isFinished() {
        return slideInt.isFinished() && visibilityInt.isFinished();
    }

    @Override
    public void reset() {
        slideInt.reset();
        visibilityInt.reset();
    }
}
