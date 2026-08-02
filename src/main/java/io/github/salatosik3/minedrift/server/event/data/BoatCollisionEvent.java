package io.github.salatosik3.minedrift.server.event.data;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;

public class BoatCollisionEvent implements Event {
    private final ServerPlayer serverPlayer;
    private final Entity boat;

    public BoatCollisionEvent(ServerPlayer serverPlayer, Entity boat) {
        this.serverPlayer = serverPlayer;
        this.boat = boat;
    }

    public ServerPlayer getServerPlayer() {
        return serverPlayer;
    }

    public Entity getBoat() {
        return boat;
    }
}
