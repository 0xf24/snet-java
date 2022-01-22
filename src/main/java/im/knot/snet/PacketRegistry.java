package im.knot.snet;

import im.knot.snet.payload.Err;
import im.knot.snet.payload.Invalid;
import im.knot.snet.payload.Ok;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * this is the most fragile code here. replace with shitn't
 */
public class PacketRegistry {

    private final HashMap<Class<? extends Packet>, Integer> ids;

    private final ArrayList<PacketBuilder<? extends Packet>> builders;

    public PacketRegistry() {
        this.builders = new ArrayList<>();
        this.ids = new HashMap<>();
        builders.add(ERR.builder);
        builders.add(OK.builder);
        ids.put(ERR.clazz, 0);
        ids.put(OK.clazz,  1);
    }



    public int id(Packet packet) {
        Class<? extends Packet> clazz = packet.getClass();

        Integer id = ids.get(clazz);
        if (id == null) return -1;
        return id;
    }

    public PacketBuilder<? extends Packet> get(short id) {
        PacketBuilder<? extends Packet> builder = builders.get(id);

        if (builder == null) {
            return Invalid.BUILDER;
        }
        return builder;
    }


    //=== static ===//

    protected static final HashMap<String, PacketType> TYPES = new HashMap<>();

    private static final ArrayList<String> NAMES = new ArrayList<>();


    private static final PacketType ERR = new PacketType(Err.class, (r) -> new Err(r.getString()));
    private static final PacketType OK = new PacketType(Ok.class, (r) -> Ok.OK);

    public static final PacketRegistry SERVER = new PacketRegistry();

    public static void send(int api, int ver, SWriter writer) {
        writer.putByte((byte) api);
        writer.putShort((short) ver);
        writer.putShort((short) NAMES.size());

        System.out.printf("sending %d names%n", NAMES.size());
        for (String name : NAMES) {
            System.out.printf("sending name %s%n", name);
            writer.putString(name);
        }
    }

    public static PacketRegistry createClient(SReader reader) {
        int api = reader.getUByte();
        int ver = reader.getUShort();

        System.out.printf("api: %d ver :%d%n", api, ver);

        int size = reader.getUShort();

        System.out.printf("getting %d types%n", size);

        PacketRegistry registry = new PacketRegistry();

        for (int i = 2; i < size; i++) {
            String name = reader.getString();

            PacketType type = TYPES.get(name);

            registry.ids.put(type.clazz, i);
            registry.builders.add(type.builder);
        }

        return registry;
    }

    public static void register(String name, Class<? extends Packet> clazz, PacketBuilder<? extends Packet> builder) {
        TYPES.put(name, new PacketType(clazz, builder));
        NAMES.add(name);
        int id = SERVER.builders.size();
        SERVER.builders.add(builder);
        SERVER.ids.put(clazz, id);
    }



    private static class PacketType {
        public final Class<? extends Packet> clazz;
        public final PacketBuilder<? extends Packet> builder;

        private PacketType(Class<? extends Packet> clazz, PacketBuilder<? extends Packet> builder) {
            this.clazz = clazz;
            this.builder = builder;
        }
    }
}
