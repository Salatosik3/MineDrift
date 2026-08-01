package io.github.salatosik3.minedrift.server.listener.fabric.drift;

import io.github.salatosik3.minedrift.server.event.ListenerInvoker;
import io.github.salatosik3.minedrift.server.event.data.BoatDriftEvent;
import io.github.salatosik3.minedrift.server.utils.VectorUtils;
import io.github.salatosik3.minedrift.server.listener.fabric.EventListener;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.function.Consumer;

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
    private final Map<UUID, Vec3> lastVehicleLoc = new HashMap<>();

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
                var lastPosVec = lastVehicleLoc.get(vehicle.getUUID());

                if (lastPosVec != null) {
                    var velocity = lastPosVec.subtract(posVec);
                    velocity = VectorUtils.nullifyNearZeroValues(velocity, SMALLEST_OPERAPABLE_VALUE);
                    onVehicleMove(player, vehicle, velocity);
                }

                lastVehicleLoc.put(vehicle.getUUID(), posVec);
            }
        });
    }

    private void demoParticle(Level level, double x, double y, double z) {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.COMPOSTER, x, y + 2, z, 10, 0, 0, 0, 0.1);
        }
    }

    private void demoParticle(Level level, Vec3 vec) {
        demoParticle(level, vec.x, vec.y, vec.z);
    }

    private void raytrace(Level level, Vec3 boatPosition, Vec3 boatVelocityDirection, Consumer<Vec3> consumer) {
        Vec3 positionOffset = boatVelocityDirection;

        while(true) {
            Vec3 offsetBoatPosition = boatPosition.add(positionOffset);
            boolean isPositionOnAir = level.getBlockState(BlockPos.containing(offsetBoatPosition)).isAir();
            if (!isPositionOnAir) {
                break;
            }

            double offsetDistance = offsetBoatPosition.distanceTo(boatPosition);
            if (offsetDistance > 5) {
                break;
            }

            consumer.accept(offsetBoatPosition);
            positionOffset = positionOffset.add(boatVelocityDirection);
        }
    }

    private void testRaytrace(Level level, Vec3 start, Vec3 velocity) {
        Vec3 normalizedVelocity = velocity.normalize();

        Consumer<Vec3> tracePositionConsumer = position -> demoParticle(level, position);
        raytrace(level, start, normalizedVelocity, tracePositionConsumer);
        raytrace(level, start, normalizedVelocity.yRot((float) Math.toRadians(90)), tracePositionConsumer);
        raytrace(level, start, normalizedVelocity.yRot((float) Math.toRadians(-90)), tracePositionConsumer);
    }

    private void onVehicleMove(ServerPlayer player, Entity boat, Vec3 vehicleVel) {
        if (!BOATS.contains(boat.getType())) {
            return;
        }

        if (vehicleVel.length() < SMALLEST_OPERAPABLE_VALUE) { // This thing is necessary because of how a computer stores floating point numbers
            return;
        }

        testRaytrace(boat.level(), boat.position(), vehicleVel); // Because of how Vec3 class was implemented (e.g Vec3.normalize() method that will return ZERO vector just because dist is less than 1.0E-5D) I have to control floating point number situation either in this method or in this method that is called by this method blah blah blah

        var vl = boat.getLookAngle().multiply(-1, 1, -1);
        double driftAngle = Math.abs(VectorUtils.calculate2DAngle(vehicleVel, vl));

        if (driftAngle >= 30 && driftAngle <= 120 && vehicleVel.length() > 0.1f) {
            onBoatDrift(player, boat, vehicleVel, driftAngle);
        }
    }

    private void onBoatDrift(ServerPlayer player, Entity boat, Vec3 boatVel, double driftAngle) {
        listenerInvoker.invoke(new BoatDriftEvent(player, boat, boatVel, driftAngle));
    }

    private void onBoatCollide(ServerPlayer player, Entity boat) {
        player.sendOverlayMessage(Component.literal("Collision detected!")); // TODO remove
    }
}
