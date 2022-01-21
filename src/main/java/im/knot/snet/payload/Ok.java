package im.knot.snet.payload;

import im.knot.snet.Packet;
import im.knot.snet.SWriter;

public class Ok implements Packet {

    private Ok() {
        //empty on purpose
    }

    @Override
    public void write(SWriter writer) {
        //empty on purpose
    }

    public static Ok OK = new Ok();
}
