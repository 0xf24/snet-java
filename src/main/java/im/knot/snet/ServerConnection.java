package im.knot.snet;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicLong;

/**
 * server side connection class
 */
public class ServerConnection extends Connection {

    private final AtomicLong counter = new AtomicLong(0);

    ServerConnection(Socket socket) throws IOException {
        super(socket);
        socket.setSoTimeout(5000);

        //bad code. please replace
        Thread thread = new Thread(() -> {

        }, "snet_con");
    }

    @Override
    protected PacketRegistry createRegistry() {
        return PacketRegistry.SERVER;
    }

    @Override
    protected boolean ensure() {
        return !socket.isClosed() && socket.isConnected();
    }

    @Override
    protected long genId() {
        return counter.incrementAndGet();
    }
}
