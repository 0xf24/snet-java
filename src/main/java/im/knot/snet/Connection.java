package im.knot.snet;

import im.knot.snet.payload.Err;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Abstract implementation of a connection. connections represent one end of a socket, and have infrastructure to send
 * and listen to packets
 */
public abstract class Connection {

    /**
     * underlying java io socket
     */
    protected Socket socket;

    protected PacketRegistry registry;

    private final HashMap<Long, CompletableFuture<Packet>> transactions;

    protected Connection(Socket socket) throws IOException {
        this.socket = socket;
        registry = createRegistry();
        transactions = new HashMap<>();
    }

    protected abstract PacketRegistry createRegistry() throws IOException;

    /**
     * ensure a socket connection
     * @return return true if connected, else false
     */
    protected abstract boolean ensure();

    /**
     * generate a free id to use for a packet
     * @return the id
     */
    protected abstract long genId();

    /**
     * send a packet through this connection.
     * @param packet the packet to send. if there is no transaction set an id will be assigned
     * @return a completable future that will complete with either a) the received packet from the connection or b)
     * an error indicating a closed socket or timeout
     */
    public CompletableFuture<Packet> send(Packet packet) {
        return send(genId(), packet);
    }

    private CompletableFuture<Packet> send(long tid, Packet packet) {
        ensure();
        try {
            SWriter output = new SWriter(socket.getOutputStream());

            output.put((short) registry.id(packet));
            output.put(tid);

            try(ByteArrayOutputStream bas = new ByteArrayOutputStream()) {
                SWriter writer = new SWriter(bas);
                packet.write(writer);

                output.put((short) bas.size());
                output.put(bas.toByteArray());

                CompletableFuture<Packet> future = new CompletableFuture<>();

                transactions.put(tid, future);

                return future;

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return CompletableFuture.completedFuture(new Err("io"));
    }

    /**
     * start listening to the socket to process events
     */
    protected void startListening() {
        try {
            InputStream stream = socket.getInputStream();

            SReader reader = new SReader(stream);

            while (!socket.isClosed() || ensure()) {

                short id = reader.getShort();
                long tid = reader.getLong();
                int size = reader.getShort() & 0xffff;

                try(ByteArrayInputStream bai = new ByteArrayInputStream(reader.get(size))) {
                    SReader packet = new SReader(bai);

                    Packet p = registry.get(id).build(packet);

                    if (transactions.containsKey(tid)) {
                        transactions.get(tid).complete(p);
                        transactions.remove(tid);
                    } else {
                        dispatch(tid, p);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private final List<ConnectionListener> listeners = new ArrayList<>();

    /**
     * listen for incoming packets. only packets that do not have a completable future waiting on their ID will be sent
     * to the listeners. Listeners can reply to a packet by returning a new packet. it is undefined behavior for more
     * than one listener to reply to a packet.
     * @param listener the listener to add
     */
    public void listen(ConnectionListener listener) {
        listeners.add(listener);
    }

    /**
     * dispatch a packet to listeners
     * @param p the packet to dispatch
     */
    private void dispatch(long tid, Packet p) {
        for (ConnectionListener listener : listeners) {
            listener.accept(p).thenAccept(r -> {
                if (r != null) {
                    send(tid, r);
                }
            });
        }
    }

    @FunctionalInterface
    public interface ConnectionListener {
        CompletableFuture<Packet> accept(Packet p);
    }
}
