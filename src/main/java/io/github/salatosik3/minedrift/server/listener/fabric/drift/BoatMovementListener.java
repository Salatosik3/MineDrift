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

    private boolean raytraceMultiDirectionally(Entity entity, Vec3 velocity) {
        Vec3 direction = velocity.normalize();
        for (int i = -90; i <= 90; i += 40) {
            Vec3 rotatedDirection = direction.yRot((float) Math.toRadians(i));
            BlockState blockState = raytrace(entity.level(), entity.position(), rotatedDirection, entity.getBoundingBox().getSize());
            if (blockState != null) {
                return true;
            }
        }
        return false;
    }

    private Vec3 lastLocation = null;

    private boolean isLocationTheSameAsPrevious(Entity entity) {
        if (lastLocation == null) {
            lastLocation = entity.position();
            return false;
        }
        Vec3 location = entity.position();
        boolean areSame = (long) lastLocation.x == (long) location.x &&
                (long) lastLocation.y == (long) location.y &&
                (long) lastLocation.z == (long) location.z;
        lastLocation = location;
        return areSame;
    }

    private Vec3 lastSlideLoc = null;

    private boolean isEntitySliding(Entity entity) {
        if (lastSlideLoc == null) {
            lastSlideLoc = entity.position();
            return false;
        }

        Vec3 currentSlideLoc = entity.position();
        boolean result = false;

        if (currentSlideLoc.x - lastSlideLoc.x == 0 && currentSlideLoc.z - lastSlideLoc.z != 0) {
            result = true;
        } else if(currentSlideLoc.z - lastSlideLoc.z == 0 && currentSlideLoc.x - lastSlideLoc.x != 0) {
            result = true;
        }

        lastSlideLoc = currentSlideLoc;

        return result;
    }

//    private long checkTime = 0;

    private int collisionCounter = 0;

    /*
    It does work not perfect but cool, but it anyway works weird.
    I think I should try to also check if e.g a coordinate is staying the same and another one is changing, so I can detect if a player just slides by a line of blocks.
    In general this kind of mechanics needs to research many different situations, there is no single line logic...
     */
    private boolean checkCollision(Entity entity, Vec3 velocity) {

//        long currentTime = System.currentTimeMillis();
//        if (currentTime - checkTime > 5 * 50) {
//            checkTime = currentTime;
//        } else {
//            return false;
//        }

        Vec3 lastVelocity = lastEntityVelocities.computeIfAbsent(entity.getUUID(), _ -> velocity);
        lastEntityVelocities.put(entity.getUUID(), velocity);

        if (!isLocationTheSameAsPrevious(entity)) {
            return false;
        }

//        MineDrift.LOGGER.debug("Locations are different!");

        if (isEntitySliding(entity)) {
            MineDrift.LOGGER.debug("The entity is sliding");
            return true;
        }

        if (velocity.length() > lastVelocity.length()) {
            return false;
        }

        double speedFactor = velocity.length() / lastVelocity.length();

        if (speedFactor < 0.60 && raytraceMultiDirectionally(entity, velocity)) {
            MineDrift.LOGGER.debug("Collision detected! ({})", collisionCounter++);
            return true;
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
