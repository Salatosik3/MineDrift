package io.github.salatosik3.minedrift.server.service.impl;

import io.github.salatosik3.minedrift.misc.SimpleTimerTask;
import io.github.salatosik3.minedrift.networking.client.DriftPayload;
import io.github.salatosik3.minedrift.networking.client.DriftState;
import io.github.salatosik3.minedrift.networking.client.DriftStatePayload;
import io.github.salatosik3.minedrift.server.service.DriftPacketService;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DriftPacketServiceImpl implements DriftPacketService, SimpleTimerTask {
    private final PlayerList playerList;

    public DriftPacketServiceImpl(Timer timer, PlayerList playerList) {
        this.playerList = playerList;
        timer.scheduleAtFixedRate(this.asTimerTaskClass(), 0, 500);
    }

    @Override
    public void run() {

    }

    @Override
    public void notifyDrifting(ServerPlayer player, int score) {

    }

    @Override
    public void notifyFail(ServerPlayer player) {

    }

    @Override
    public void notifyEndDrifting(ServerPlayer player) {

    }
}
