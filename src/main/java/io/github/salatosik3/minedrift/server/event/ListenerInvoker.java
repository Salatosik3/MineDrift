package io.github.salatosik3.minedrift.server.event;

import io.github.salatosik3.minedrift.server.event.data.Event;

public interface ListenerInvoker {
    <T extends Event> void invoke(T event);
}
