package io.github.salatosik3.minedrift.event;

import io.github.salatosik3.minedrift.event.data.Event;

public interface ListenerInvoker {
    <T extends Event> void invoke(T event);
}
