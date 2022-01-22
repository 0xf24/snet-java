import im.knot.snet.*;
import im.knot.snet.payload.Err;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.CompletableFuture;

public class Main {

    public static class Foo implements Packet {
        public final String msg;
        public Foo(String message) {
            msg = message;
        }

        @Override
        public void write(SWriter writer) {
            writer.putString(msg);
        }

        static {
        }
    }

    public static class Bar implements Packet {
        public final String msg;
        public Bar(String message) {
            msg = message;
        }

        @Override
        public void write(SWriter writer) {
            writer.putString(msg);
        }

        static {
        }
    }

    public static void main(String[] args) throws IOException {
        PacketRegistry.register("bar", Foo.class, r -> new Bar(r.getString()));
        PacketRegistry.register("foo", Foo.class, r -> new Foo(r.getString()));


        Server server = new Server(1234, (c) -> {
            c.listen(new Logger("server"));
            c.listen(p -> {
//                System.out.println("new packet");
                if (p instanceof Foo) {
//                    System.out.println("foo packet");
                    return CompletableFuture.completedFuture(new Bar("test2"));
                } else {
//                    System.out.println("invalid packet");
                    return CompletableFuture.completedFuture(new Err("unexpected type"));
                }
            });
        });

        ClientConnection client = new ClientConnection(InetAddress.getLocalHost(), 1234);

        client.listen(new Logger("client"));

        client.send(new Foo("test1"));
    }

    private static class Logger implements Connection.ConnectionListener {
        public final String name;

        private Logger(String name) {
            this.name = name;
        }

        @Nullable
        @Override
        public CompletableFuture<Packet> accept(Packet p) {
            System.out.printf(
                    "[%s] received packet %s%n%s%n",
                    name,
                    p.getClass().getSimpleName(),
                    p
            );

            return null;
        }
    }
}
