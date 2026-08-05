package io.github.salatosik3.minedrift.server.event;

import io.github.salatosik3.minedrift.server.event.data.Event;

import java.util.List;

public interface BusEventListener {
    List<ListenerEntry<? extends Event>> getListeners();
}
