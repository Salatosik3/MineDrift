package io.github.salatosik3.minedrift.server.listener.fabric.drift;

import io.github.salatosik3.minedrift.server.MineDrift;
import io.github.salatosik3.minedrift.server.event.ListenerInvoker;
import io.github.salatosik3.minedrift.server.event.data.BoatCollisionEvent;
import io.github.salatosik3.minedrift.server.event.data.BoatDriftEvent;
import io.github.salatosik3.minedrift.server.utils.VectorUtils;
import io.github.salatosik3.minedrift.server.listener.fabric.EventListener;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.io.PipedOutputStream;
import java.util.*;

public class BoatMovementListener implements EventListener {
    private static final double SMALLEST_OPERAPABLE_VALUE = 1.0E-2D;
    private static final List<EntityType<?>> BOATS = List.of(
            EntityTypes.ACACIA_BOAT,
            EntityTypes.BIRCH_BOAT,
            EntityTypes.CHERRY_BOAT,
            EntityTypes.JUNGLE_BOAT,
            EntityTypes.DARK_OAK_BOAT,
            EntityTypes.MANGROVE_BOAT,
            EntityTypes.OAK_BOAT,
            EntityTypes.PALE_OAK_BOAT,
            EntityTypes.SPRUCE_BOAT
    );
    private final ListenerInvoker listenerInvoker;

    private final Map<UUID, Vec3> lastEntityPositions = new HashMap<>();
    private final Map<UUID, Vec3> lastEntityVelocities = new HashMap<>();
    private final Map<UUID, Long> lastCollisionCheckTimeMap = new HashMap<>();

    public BoatMovementListener(ListenerInvoker listenerInvoker) {
        this.listenerInvoker = listenerInvoker;
    }

    @Override
    public void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            var players = server.getPlayerList().getPlayers();
            for (ServerPlayer player : players) {
                var vehicle = player.getVehicle();

                if (vehicle == null) {
                    continue;
                }

                var posVec = new Vec3(vehicle.getX(), vehicle.getY(), vehicle.getZ());
                var lastPosVec = lastEntityPositions.get(vehicle.getUUID());

                if (lastPosVec != null) {
                    var velocity = posVec.subtract(lastPosVec);
                    velocity = VectorUtils.nullifyNearZeroValues(velocity, SMALLEST_OPERAPABLE_VALUE);
                    onVehicleMove(player, vehicle, velocity, lastPosVec);
                }

                lastEntityPositions.put(vehicle.getUUID(), posVec);
            }
        });
    }

    private @Nullable BlockState raytrace(Level level, Vec3 startPosition, Vec3 direction, double maxDistance) {
        if (direction.lengthSqr() == 0) {
            return null;
        }

        Vec3 currentPos = startPosition;
        double stepLength = direction.length();
        double traveledDistance = 0.0;

        while (traveledDistance <= maxDistance) {
            BlockPos blockPos = BlockPos.containing(currentPos);
            BlockState state = level.getBlockState(blockPos);

            if (!state.isAir()) {
                return state;
            }

            currentPos = currentPos.add(direction);
            traveledDistance += stepLength;
        }

        return null;
    }

    private boolean raytraceMultiDirectionally(Entity entity, Vec3 velocity) {
        Vec3 normalizedVelocity = velocity.normalize();
        Vec3 startPosition = entity.position().add(0, 0.3, 0);

        for (int i = -70; i <= 70; i += 10) {
            Vec3 rayDirection = normalizedVelocity.yRot((float) Math.toRadians(i));
            Vec3 traceStep = rayDirection.scale(0.15);

            BlockState blockState = raytrace(entity.level(), startPosition, traceStep, 1.6);
            if (blockState != null) {
                return true;
            }
        }
        return false;
    }

    private boolean isLocationTheSameAsPrevious(Vec3 actualLocation, Vec3 lastLocation) {
        return actualLocation.distanceToSqr(lastLocation) < 0.000001;
    }

    private boolean isEntitySliding(Vec3 actualLocation, Vec3 lastLocation) {
        double deltaX = Math.abs(actualLocation.x - lastLocation.x);
        double deltaZ = Math.abs(actualLocation.z - lastLocation.z);
        return deltaX < 0.01 && deltaZ > 0.05 || deltaZ < 0.01 && deltaX > 0.05;
    }

    private boolean checkCollision(Entity entity, Vec3 velocity, Vec3 lastLocation) {
        Vec3 lastVelocity = lastEntityVelocities.computeIfAbsent(entity.getUUID(), _ -> velocity);
        lastEntityVelocities.put(entity.getUUID(), velocity);

        Vec3 actualLocation = entity.position();

        if (isLocationTheSameAsPrevious(actualLocation, lastLocation)) {
            return false;
        }

        Vec3 horizontalVelocity = new Vec3(velocity.x, 0, velocity.z);
        Vec3 lastHorizontalVelocity = new Vec3(lastVelocity.x, 0, lastVelocity.z);

        if (horizontalVelocity.length() > lastHorizontalVelocity.length()) {
            return false;
        }

        double speedFactor = lastHorizontalVelocity.length() > 0
                ? horizontalVelocity.length() / lastHorizontalVelocity.length()
                : 1.0;

        if (speedFactor > 0.95) {
            return false;
        }

        if (!raytraceMultiDirectionally(entity, lastHorizontalVelocity)) {
            return false;
        }

        if (isEntitySliding(actualLocation, lastLocation)) {
            Vec3 movementDir = actualLocation.subtract(lastLocation).normalize();

            if (movementDir.lengthSqr() > 0) {
                Vec3 startPos = actualLocation.add(0, 0.3, 0);
                BlockState stateAhead = raytrace(entity.level(), startPos, movementDir.scale(0.15), 1.5);
                return stateAhead != null;
            }
            return false;
        }

        return true;
    }

    private void onVehicleMove(ServerPlayer player, Entity boat, Vec3 velocity, Vec3 lastLocation) {
        if (!BOATS.contains(boat.getType())) {
            return;
        }

        if (velocity.length() < SMALLEST_OPERAPABLE_VALUE) { // This thing is necessary because of how a computer stores floating point numbers
            return;
        }

        if (checkCollision(boat, velocity, lastLocation)) {
            listenerInvoker.invoke(new BoatCollisionEvent(player, boat));
        }

        double driftAngle = Math.abs(VectorUtils.calculate2DAngle(velocity, boat.getLookAngle()));

        if (driftAngle > BoatDriftEvent.MIN_DRIFT_ANGLE && driftAngle < BoatDriftEvent.MAX_DRIFT_ANGLE && velocity.length() > 0.1f) {
            listenerInvoker.invoke(new BoatDriftEvent(player, boat, velocity, driftAngle));
        }
    }
}
