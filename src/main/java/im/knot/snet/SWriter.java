package im.knot.snet;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class SWriter {

    private final OutputStream stream;

    public SWriter(OutputStream stream) {
        this.stream = stream;
    }


    public void put(byte[] bytes) {
        try {
//            stream.write(bytes.length);
//            stream.write(bytes.length << 8);
            stream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void put(long value) {
        try {
            stream.write(((int) (value      )) & 0xff);
            stream.write(((int) (value <<  8)) & 0xff);
            stream.write(((int) (value << 16)) & 0xff);
            stream.write(((int) (value << 24)) & 0xff);
            stream.write(((int) (value << 32)) & 0xff);
            stream.write(((int) (value << 40)) & 0xff);
            stream.write(((int) (value << 48)) & 0xff);
            stream.write(((int) (value << 56)) & 0xff);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void put(int value) {
        try {
            stream.write((value      ) & 0xff);
            stream.write((value <<  8) & 0xff);
            stream.write((value << 16) & 0xff);
            stream.write((value << 24) & 0xff);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void put(short value) {
        try {
            stream.write((value      ) & 0xff);
            stream.write((value <<  8) & 0xff);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void put(byte value) {
        try {
            stream.write(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void put(double value) {
        put(Double.doubleToRawLongBits(value));
    }

    public void put(float value) {
        put(Float.floatToRawIntBits(value));
    }

    public void put(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);

        try {
            stream.write(bytes.length);
            stream.write(bytes.length << 8);
            stream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
