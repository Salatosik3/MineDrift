package io.github.salatosik3.minedrift.server.event.data;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class BoatDriftEvent implements Event {
    private final ServerPlayer serverPlayer;
    private final Entity boat;
    private final Vec3 boatVelocity;
    private final double driftAngle;

    public BoatDriftEvent(ServerPlayer serverPlayer, Entity boat, Vec3 boatVelocity, double driftAngle) {
        this.serverPlayer = serverPlayer;
        this.boat = boat;
        this.boatVelocity = boatVelocity;
        this.driftAngle = driftAngle;
    }

    public ServerPlayer getServerPlayer() {
        return serverPlayer;
    }

    public Entity getBoat() {
        return boat;
    }

    public Vec3 getBoatVelocity() {
        return boatVelocity;
    }

    public double getDriftAngle() {
        return driftAngle;
    }
}
