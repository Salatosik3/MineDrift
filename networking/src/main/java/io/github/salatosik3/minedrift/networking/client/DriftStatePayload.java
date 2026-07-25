package io.github.salatosik3.minedrift.networking.client;

import io.github.salatosik3.minedrift.networking.CustomPayloadRegistrar;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;

public record DriftStatePayload(String stateName) implements CustomPacketPayload {
    public DriftStatePayload {
        boolean match = Arrays.stream(DriftState.values()).anyMatch(s -> s.name().equals(stateName));
        if (!match) {
            throw new IllegalArgumentException("No such state named %s".formatted(stateName));
        }
    }

    public DriftStatePayload(DriftState driftState) {
        this(driftState.name());
    }

    public static final Identifier ID = Identifier.fromNamespaceAndPath(CustomPayloadRegistrar.MOD_ID, "drift_state");
    public static final CustomPacketPayload.Type<DriftPayload> TYPE = new CustomPacketPayload.Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, DriftStatePayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, DriftStatePayload::stateName,
            DriftStatePayload::new
    );

    public DriftState state() {
        return DriftState.valueOf(stateName);
    }

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
