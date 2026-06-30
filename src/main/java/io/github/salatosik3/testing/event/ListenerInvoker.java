package io.github.salatosik3.testing.event;

import io.github.salatosik3.testing.event.data.Event;

public interface ListenerInvoker {
    <T extends Event> void invoke(T event);
}
