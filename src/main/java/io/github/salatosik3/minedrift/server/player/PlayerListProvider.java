package io.github.salatosik3.minedrift.server.player;

import net.minecraft.server.players.PlayerList;

@FunctionalInterface
public interface PlayerListProvider {
    PlayerList getPlayerList();
}
