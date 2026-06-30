package io.github.salatosik3.minedrift.listener.fabric.drift;

import io.github.salatosik3.minedrift.event.ListenerInvoker;
import io.github.salatosik3.minedrift.event.data.BoatDriftEvent;
import io.github.salatosik3.minedrift.utils.VectorUtils;
import io.github.salatosik3.minedrift.listener.fabric.EventListener;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BoatDriftListener implements EventListener {
    private static final double SMALLEST_OPERAPABLE_VALUE = 0e-2d;
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

    public BoatDriftListener(ListenerInvoker listenerInvoker) {
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

    private void onVehicleMove(ServerPlayer player, Entity boat, Vec3 boatVel) {
        if (!BOATS.contains(boat.getType())) {
            return;
        }
        if (boatVel.length() < SMALLEST_OPERAPABLE_VALUE) {
            return;
        }

        var vl = boat.getLookAngle().multiply(-1, 1, -1);
        double driftAngle = Math.abs(VectorUtils.calculate2DAngle(boatVel, vl));

        if (driftAngle >= 15 && driftAngle <= 120) {
            onBoatDrift(player, boat, boatVel, driftAngle);
        }
    }

    private void onBoatDrift(ServerPlayer player, Entity boat, Vec3 boatVel, double driftAngle) {
        listenerInvoker.invoke(new BoatDriftEvent(player, boat, boatVel, driftAngle));
    }
}
