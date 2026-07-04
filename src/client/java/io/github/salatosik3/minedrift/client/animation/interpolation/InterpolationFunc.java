package io.github.salatosik3.minedrift.client.animation.interpolation;

@FunctionalInterface
public interface InterpolationFunc {
    double interpolate(double a, double b, double t);
}
