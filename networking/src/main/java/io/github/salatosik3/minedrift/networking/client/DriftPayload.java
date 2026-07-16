package io.github.salatosik3.minedrift.networking.client;

import io.github.salatosik3.minedrift.networking.Networking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public record DriftPayload(double driftAngle) implements CustomPacketPayload {

    public static final Identifier DRIFT_PAYLOAD_ID = Identifier.fromNamespaceAndPath(Networking.MOD_ID, "drift");
    public static final CustomPacketPayload.Type<DriftPayload> TYPE = new CustomPacketPayload.Type<>(DRIFT_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, DriftPayload> CODEC = StreamCodec.composite(ByteBufCodecs.DOUBLE, DriftPayload::driftAngle, DriftPayload::new);

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
