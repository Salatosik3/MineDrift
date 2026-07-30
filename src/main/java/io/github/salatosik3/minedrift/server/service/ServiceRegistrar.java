package io.github.salatosik3.minedrift.server.service;

import io.github.salatosik3.minedrift.misc.Registrar;
import io.github.salatosik3.minedrift.server.player.PlayerListProvider;
import io.github.salatosik3.minedrift.server.service.impl.DriftPacketServiceImpl;
import io.github.salatosik3.minedrift.server.timer.TimerProvider;
import net.minecraft.server.players.PlayerList;

public class ServiceRegistrar extends Registrar {

    private final TimerProvider timerProvider;
    private final PlayerListProvider playerListProvider;

    public ServiceRegistrar(TimerProvider timerProvider, PlayerListProvider playerListProvider) {
        this.timerProvider = timerProvider;
        this.playerListProvider = playerListProvider;
    }

    public void registerAll() {
        register(DriftPacketService.class, new DriftPacketServiceImpl(timerProvider.getTimer(), playerListProvider.getPlayerList()));
    }
}
