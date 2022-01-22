package im.knot.snet;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class SWriter {

    private final OutputStream stream;

    public SWriter(OutputStream stream) {
        this.stream = stream;
    }


    public void put(byte[] bytes) throws IOException {
//            stream.write(bytes.length);
//            stream.write(bytes.length << 8);
        stream.write(bytes);
    }

    public void putLong(long value) throws IOException {
        stream.write((int) (value       ));
        stream.write((int) (value >>>  8));
        stream.write((int) (value >>> 16));
        stream.write((int) (value >>> 24));
        stream.write((int) (value >>> 32));
        stream.write((int) (value >>> 40));
        stream.write((int) (value >>> 48));
        stream.write((int) (value >>> 56));
    }

    public void putInt(int value) throws IOException {
        stream.write(value);
        stream.write(value >>>  8);
        stream.write(value >>> 16);
        stream.write(value >>> 24);
    }

    public void putUInt(long value) throws IOException {
        putInt((int)(value));
    }

    public void putShort(short value) throws IOException {
        stream.write(value);
        stream.write(value >>> 8);
    }

    public void putUShort(int value) throws IOException {
        stream.write(value);
        stream.write(value >>> 8);
    }

    public void putByte(byte value) throws IOException {
        stream.write(value);
    }

    public void putUByte(int value) throws IOException {
        stream.write(value);
    }

    public void putDouble(double value) throws IOException {
        putLong(Double.doubleToRawLongBits(value));
    }

    public void putFloat(float value) throws IOException {
        putInt(Float.floatToRawIntBits(value));
    }

    public void putString(String value) throws IOException {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);

        stream.write(bytes.length);
        stream.write(bytes.length >>> 8);
        stream.write(bytes);
    }
}
