package im.knot.snet;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface PacketBuilder<T extends Packet> {

    @Nonnull
    T build(@Nonnull SReader reader);

}
