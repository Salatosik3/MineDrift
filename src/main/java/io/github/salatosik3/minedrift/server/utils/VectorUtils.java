package io.github.salatosik3.minedrift.server.utils;

import net.minecraft.world.phys.Vec3;

public final class VectorUtils {
    private VectorUtils() {
        /* utility class */
    }

    public static double calculate2DAngle(Vec3 v1, Vec3 v2) {
        double dot = v1.x * v2.x + v1.z * v2.z;
        double det = v1.x * v2.z - v1.z * v2.x;
        return Math.atan2(det, dot) * 180 / Math.PI;
    }

    public static Vec3 nullifyNearZeroValues(Vec3 vec, double ceiling) {
        var changedVector = vec;
        if (Math.abs(vec.x) < ceiling) {
            changedVector = vec.multiply(0, 1, 1);
        }
        if (Math.abs(vec.y) < ceiling) {
            changedVector = vec.multiply(1, 0, 1);
        }
        if (Math.abs(vec.z) < ceiling) {
            changedVector = vec.multiply(1, 1, 0);
        }
        return changedVector;
    }
}
