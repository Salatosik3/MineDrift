package io.github.salatosik3.minedrift.server.listener.fabric.drift;

import io.github.salatosik3.minedrift.server.event.ListenerInvoker;
import io.github.salatosik3.minedrift.server.event.data.BoatCollisionEvent;
import io.github.salatosik3.minedrift.server.event.data.BoatDriftEvent;
import io.github.salatosik3.minedrift.server.utils.VectorUtils;
import io.github.salatosik3.minedrift.server.listener.fabric.EventListener;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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
    private static final double MIN_DRIFT_ANGLE = 15;

    private final Map<UUID, Vec3> lastEntityPositions = new HashMap<>();
    private final ListenerInvoker listenerInvoker;

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

    private boolean checkCollision(Entity entity, Vec3 velocity) {
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

    private void onVehicleMove(ServerPlayer player, Entity boat, Vec3 vehicleVel) {
        if (!BOATS.contains(boat.getType())) {
            return;
        }

        if (vehicleVel.length() < SMALLEST_OPERAPABLE_VALUE) { // This thing is necessary because of how a computer stores floating point numbers
            return;
        }

        if (checkCollision(boat, vehicleVel)) {
            onBoatCollide(player, boat);
        }

        double driftAngle = Math.abs(VectorUtils.calculate2DAngle(vehicleVel, boat.getLookAngle()));

        if (driftAngle > MIN_DRIFT_ANGLE && vehicleVel.length() > 0.1f) {
            player.sendOverlayMessage(Component.literal(String.valueOf(driftAngle)));
            onBoatDrift(player, boat, vehicleVel, driftAngle);
        }
    }

    private void onBoatDrift(ServerPlayer player, Entity boat, Vec3 boatVel, double driftAngle) {
        listenerInvoker.invoke(new BoatDriftEvent(player, boat, boatVel, driftAngle));
    }

    private void onBoatCollide(ServerPlayer player, Entity boat) {
        listenerInvoker.invoke(new BoatCollisionEvent(player, boat));
    }
}
