package im.knot.snet;

import javax.annotation.CheckForNull;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SReader {

    private final InputStream stream;

    public SReader(InputStream stream) {
        this.stream = stream;
    }

    public byte[] get(int size) {
        byte[] bytes = new byte[size];

        try {
            stream.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    public long getLong() {
        long v = 0;

        try {
            v = ((long) stream.read())      ;
            v = ((long) stream.read()) <<  8;
            v = ((long) stream.read()) << 16;
            v = ((long) stream.read()) << 24;
            v = ((long) stream.read()) << 32;
            v = ((long) stream.read()) << 40;
            v = ((long) stream.read()) << 48;
            v = ((long) stream.read()) << 56;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return v;
    }

    public int getInt() {
        int v = 0;

        try {
            v = stream.read()      ;
            v = stream.read() <<  8;
            v = stream.read() << 16;
            v = stream.read() << 24;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return v;
    }

    public short getShort() {
        short v = 0;

        try {
            v = (short) (stream.read()     );
            v = (short) (stream.read() << 8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return v;
    }

    public int getUShort() {
        return getShort() & 0xFFFF;
    }

    public byte getByte() {
        byte v = 0;

        try {
            v = (byte) (stream.read() & 0xff);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return v;
    }

    public int getUByte() {
        return getByte() & 0xff;
    }

    public double getDouble() {
        return Double.longBitsToDouble(getLong());
    }

    public float getFloat() {
        return Float.intBitsToFloat(getInt());
    }

    public String getString() {
        int len = getShort() & 0xffff;

        byte[] raw = new byte[len];

        try {
            stream.read(raw, 0, len);

            return new String(raw, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
