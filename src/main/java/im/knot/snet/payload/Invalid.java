package im.knot.snet.payload;

import im.knot.snet.Packet;
import im.knot.snet.PacketBuilder;
import im.knot.snet.SWriter;

public class Invalid implements Packet {
    @Override
    public void write(SWriter writer) {
        //empty on purpose
    }

    public static final PacketBuilder<Invalid> BUILDER = (r) -> new Invalid();
}
