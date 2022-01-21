package im.knot.snet;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class Server {

    public final int port;

    private final ServerSocket socket;

    public Server(int port, Consumer<Connection> consumer) throws IOException {
        this.port = port;

        socket = new ServerSocket(port);

        new Thread(() -> {
            while (true) {
                try {
                    Socket client = socket.accept();

                    ServerConnection con = new ServerConnection(client);

                    consumer.accept(con);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, "snet_server").start();
    }

}
