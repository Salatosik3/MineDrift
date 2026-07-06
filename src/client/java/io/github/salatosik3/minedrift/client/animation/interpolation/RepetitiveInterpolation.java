package io.github.salatosik3.minedrift.client.animation.interpolation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RepetitiveInterpolation <T> implements Interpolation<T> {

    private final Interpolation<T> interpolation;
    private final int repeatAmount;

    private int repeatCounter = 0;

    private final List<Consumer<Interpolation<T>>> callbacks = new ArrayList<>();

    public RepetitiveInterpolation(Interpolation<T> interpolation, int repeatAmount) {
        this.interpolation = interpolation;
        this.repeatAmount = repeatAmount;
    }

    private boolean isInfinity() {
        return repeatAmount <= 0;
    }

    @Override
    public T get() {
        if (interpolation.isEnded() && (isInfinity() || repeatCounter != repeatAmount)) {
            interpolation.restart();
            repeatCounter++;
        }
        return interpolation.get();
    }

    @Override
    public boolean isEnded() {
        if (isInfinity()) {
            return false; // there is no end muhahahaha
        }
        return repeatAmount == repeatCounter;
    }

    @Override
    public void restart() {
        interpolation.restart();
        repeatCounter = 0;
    }

    @Override
    public boolean isReverse() {
        return interpolation.isReverse();
    }

    @Override
    public void setReverse(boolean reverse) {
        interpolation.setReverse(reverse);
    }

    @Override
    public InterpolationType getType() {
        return interpolation.getType();
    }

    @Override
    public void setType(InterpolationType type) {
        interpolation.setType(type);
    }

    @Override
    public void addOnEndCallback(Consumer<Interpolation<T>> callback) {
        callbacks.add(callback);
    }
}
