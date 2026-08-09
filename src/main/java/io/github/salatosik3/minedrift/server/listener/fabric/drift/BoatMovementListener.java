package io.github.salatosik3.minedrift.server.listener.fabric.drift;

import io.github.salatosik3.minedrift.server.MineDrift;
import io.github.salatosik3.minedrift.server.event.ListenerInvoker;
import io.github.salatosik3.minedrift.server.event.data.BoatCollisionEvent;
import io.github.salatosik3.minedrift.server.event.data.BoatDriftEvent;
import io.github.salatosik3.minedrift.server.utils.VectorUtils;
import io.github.salatosik3.minedrift.server.listener.fabric.EventListener;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

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
                    onVehicleMove(player, vehicle, velocity);
                }

                lastEntityPositions.put(vehicle.getUUID(), posVec);
            }
        });
    }

    private @Nullable BlockState raytrace(Level level, Vec3 startPosition, Vec3 direction, double maxDistance) {
        if (direction.length() == 0) {
            return null;
        }

        Vec3 positionOffset = direction;

        while(true) {
            Vec3 offsetBoatPosition = startPosition.add(positionOffset);

            double offsetDistance = offsetBoatPosition.distanceTo(startPosition);
            if (offsetDistance > maxDistance) {
                break;
            }

            BlockState offsetPositionBlockState = level.getBlockState(BlockPos.containing(offsetBoatPosition));

            if (!offsetPositionBlockState.isAir()) {
                return offsetPositionBlockState;
            }

            positionOffset = positionOffset.add(direction);
        }

        return null;
    }

    private long checkTime = 0;

    private boolean checkCollision(Entity entity, Vec3 velocity) {

        long currentTime = System.currentTimeMillis();
        if (currentTime - checkTime > 10 * 50) {
            checkTime = currentTime;
        } else {
            return false;
        }

        Vec3 lastVelocity = lastEntityVelocities.computeIfAbsent(entity.getUUID(), _ -> velocity);
        lastEntityVelocities.put(entity.getUUID(), velocity);

        if (velocity.length() < lastVelocity.length()) {
            double speedFactor = velocity.length() / lastVelocity.length();

            if (speedFactor < 0.50) {
                BlockState raytracedBlockState = raytrace(entity.level(), entity.position(), velocity, 2);

                if (raytracedBlockState != null) {
                    MineDrift.LOGGER.debug("Collision detected! Speed factor: {}.2f, raytraced block: {}", speedFactor, raytracedBlockState.getBlock().getName());
                    return true;
                }
            }
        }

        return false;
    }

    private void onVehicleMove(ServerPlayer player, Entity boat, Vec3 velocity) {
        if (!BOATS.contains(boat.getType())) {
            return;
        }

        if (velocity.length() < SMALLEST_OPERAPABLE_VALUE) { // This thing is necessary because of how a computer stores floating point numbers
            return;
        }

        if (checkCollision(boat, velocity)) {
            listenerInvoker.invoke(new BoatCollisionEvent(player, boat));
        }

        double driftAngle = Math.abs(VectorUtils.calculate2DAngle(velocity, boat.getLookAngle()));

        if (driftAngle > BoatDriftEvent.MIN_DRIFT_ANGLE && driftAngle < BoatDriftEvent.MAX_DRIFT_ANGLE && velocity.length() > 0.1f) {
            listenerInvoker.invoke(new BoatDriftEvent(player, boat, velocity, driftAngle));
        }
    }
}
