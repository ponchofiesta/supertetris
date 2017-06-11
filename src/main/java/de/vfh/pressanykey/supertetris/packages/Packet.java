package de.vfh.pressanykey.supertetris.packages;


import de.vfh.pressanykey.supertetris.network.GameServer;
import de.vfh.pressanykey.supertetris.network.PlayerClient;

import java.io.UnsupportedEncodingException;

public abstract class Packet {

    public enum PacketTypes {
        INVALID(-1), LOGIN(00), DISCONNECT(01);

        private int packetId;

        private PacketTypes(int packetId) {
            this.packetId = packetId;
        }

        public int getId() {
            return packetId;
        }
    }

    public byte packetId;

    public Packet(int packetId) {
        this.packetId = (byte) packetId;
    }

    public abstract void writeData(PlayerClient client);
    public abstract void writeData(GameServer server);

    public abstract byte[] getData();

    public String readData(byte[] data) {
        try {
            String message = new String(data, "UTF-8").trim();
            return message.substring(2);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "FEHLERHAFTE DATEN";
    }

    public static PacketTypes lookupPacket(String id) {
        try {
            return lookupPacket(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            return PacketTypes.INVALID;
        }
    }


    public static PacketTypes lookupPacket(int id) {
        for(PacketTypes p : PacketTypes.values()) {
            if(p.getId() == id) {
                return p;
            }
        }
        return PacketTypes.INVALID;
    }
}
