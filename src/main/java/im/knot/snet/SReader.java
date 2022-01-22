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

    public byte[] get(int size) throws IOException {
        byte[] bytes = new byte[size];

        //should this be replaced?
        stream.read(bytes, 0, size);

        return bytes;
    }

    public long getLong() throws IOException {
        long v = 0;

        v |= ((long) stream.read())      ;
        v |= ((long) stream.read()) <<  8;
        v |= ((long) stream.read()) << 16;
        v |= ((long) stream.read()) << 24;
        v |= ((long) stream.read()) << 32;
        v |= ((long) stream.read()) << 40;
        v |= ((long) stream.read()) << 48;
        v |= ((long) stream.read()) << 56;

        return v;
    }

    public int getInt() throws IOException {
        int v = 0;

        v |= stream.read()      ;
        v |= stream.read() <<  8;
        v |= stream.read() << 16;
        v |= stream.read() << 24;

        return v;
    }

    public long getUInt() throws IOException {
        return ((long)getInt()) & 0xFFFF_FFFF;
    }

    public short getShort() {
        short v = 0;

        try {
            v  = (short) (stream.read()     );
            v |= (short) (stream.read() << 8);
        } catch (IOException e) {
//            e.printStackTrace();
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
//            e.printStackTrace();
        }

        return v;
    }

    public int getUByte() {
        return getByte() & 0xff;
    }

    public double getDouble() throws IOException {
        return Double.longBitsToDouble(getLong());
    }

    public float getFloat() throws IOException {
        return Float.intBitsToFloat(getInt());
    }

    public String getString() {
        int len = getShort() & 0xffff;

        byte[] raw = new byte[len];

        try {
            stream.read(raw, 0, len);

            return new String(raw, StandardCharsets.UTF_8);
        } catch (IOException e) {
//            e.printStackTrace();
        }

        return "";
    }
}
