package im.knot.snet.payload;

import im.knot.snet.Packet;
import im.knot.snet.SWriter;

public class Err implements Packet {

    public String msg;

    public Err(String msg) {
        this.msg = !msg.isEmpty() ? msg : null;
    }

    public Err() {
        this.msg = null;
    }

    @Override
    public void write(SWriter writer) {
        writer.putString(msg);
    }

    public static Err ERR = new Err();

    @Override
    public String toString() {
        return "[Err] " + msg;
    }
}
