package io.github.salatosik3.minedrift.server.listener.fabric.drift;

import io.github.salatosik3.minedrift.server.event.ListenerInvoker;
import io.github.salatosik3.minedrift.server.event.data.BoatDriftEvent;
import io.github.salatosik3.minedrift.server.utils.VectorUtils;
import io.github.salatosik3.minedrift.server.listener.fabric.EventListener;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BoatMovementListener implements EventListener {
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

    private void onVehicleMove(ServerPlayer player, Entity boat, Vec3 vehicleVel) {
        if (!BOATS.contains(boat.getType())) {
            return;
        }
        if (vehicleVel.length() < SMALLEST_OPERAPABLE_VALUE) {
            return;
        }

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
