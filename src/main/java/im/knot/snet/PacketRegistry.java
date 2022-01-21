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

    public static final PacketRegistry SERVER = new PacketRegistry();
    protected static final HashMap<String, PacketType> TYPES = new HashMap<>();

    private static final PacketType ERR = new PacketType(Err.class, (r) -> new Err(r.getString()));
    private static final PacketType OK = new PacketType(Ok.class, (r) -> Ok.OK);

    public static PacketRegistry createClient(SReader reader) {
        int api = reader.getUByte();
        int ver = reader.getUShort();

        int size = reader.getUShort();

        PacketRegistry registry = new PacketRegistry();

        for (int i = 2; i < size + 2; i++) {
            String name = reader.getString();

            PacketType type = TYPES.get(name);

            registry.ids.put(type.clazz, i);
            registry.builders.add(type.builder);
        }

        return registry;
    }

    public static <T extends Packet> void register(String name, Class<? extends T> clazz, PacketBuilder<T> builder) {
        TYPES.put(name, new PacketType(clazz, (PacketBuilder<Packet>) builder));
        int id = SERVER.builders.size();
        SERVER.builders.add(builder);
        SERVER.ids.put(clazz, id);
    }



    private static class PacketType {
        public final Class<? extends Packet> clazz;
        public final PacketBuilder<Packet> builder;

        private PacketType(Class<? extends Packet> clazz, PacketBuilder<Packet> builder) {
            this.clazz = clazz;
            this.builder = builder;
        }
    }
}
