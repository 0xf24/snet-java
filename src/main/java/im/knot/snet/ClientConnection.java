package im.knot.snet;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

/**
 * client side connection class
 */
public class ClientConnection extends Connection {


    private final AtomicLong counter = new AtomicLong(0);
    public final InetAddress address;
    public final int port;

    public ClientConnection(InetAddress address, int port) throws IOException {
        super(createSocket(address, port));
        this.address = address;
        this.port = port;

    }

    @Override
    protected PacketRegistry createRegistry() throws IOException {
        return PacketRegistry.createClient(new SReader(socket.getInputStream()));
    }

    @Override
    protected boolean ensure() {
        if (socket.isClosed()) {
            try {
                socket = createSocket(address, port);
                SReader reader = new SReader(socket.getInputStream());

                registry = createRegistry();

            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected long genId() {
        return counter.decrementAndGet();
    }

    private static Socket createSocket(InetAddress addr, int port) throws IOException {
//        Socket socket = SSLSocketFactory.getDefault().createSocket(addr, port);
        Socket socket = new Socket(addr, port);
        socket.setKeepAlive(true);
        socket.setSoTimeout(5000);
        return socket;
    }
}
