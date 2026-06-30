package io.github.salatosik3.testing.listener.bus;

import io.github.salatosik3.testing.event.data.BoatDriftEvent;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class BoatDriftListener implements Consumer<BoatDriftEvent> {
    @Override
    public void accept(BoatDriftEvent boatDriftEvent) {
        boatDriftEvent.getServerPlayer().sendOverlayMessage(
                Component.literal("%.2f".formatted(boatDriftEvent.getDriftAngle()))
        );
    }
}
