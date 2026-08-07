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

public class DriftPacketServiceImpl implements DriftPacketService {
    private final Map<UUID, Integer> lastScoreMap = new HashMap<>();

    private void sendStatePayload(ServerPlayer player, DriftState state) {
        ServerPlayNetworking.send(player, new DriftStatePayload(state));
    }

    @Override
    public void notifyDrifting(ServerPlayer player, int score) {
        int lastScore = lastScoreMap.computeIfAbsent(player.getUUID(), _ -> {
            sendStatePayload(player, DriftState.STARTED);
            return 0;
        });

        ServerPlayNetworking.send(player, new DriftPayload(lastScore, score));
        lastScoreMap.put(player.getUUID(), score);
    }

    @Override
    public void notifyFail(ServerPlayer player) {
        sendStatePayload(player, DriftState.FAILED);
        lastScoreMap.remove(player.getUUID());
    }

    @Override
    public void notifyEndDrifting(ServerPlayer player) {
        sendStatePayload(player, DriftState.ENDED);
        lastScoreMap.remove(player.getUUID());
    }
}
