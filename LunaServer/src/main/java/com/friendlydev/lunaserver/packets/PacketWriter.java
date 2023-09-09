package com.friendlydev.lunaserver.packets;

import com.friendlydev.lunaserver.constants.ServerConfig;
import com.friendlydev.lunaserver.constants.enums.PacketCodes.OutCode;
import com.friendlydev.lunaserver.resources.models.PlayerCharacter;
import com.friendlydev.lunaserver.resources.models.Scene;
import java.time.Instant;

/**
 *
 * @author Marc
 */
public class PacketWriter {
    public static OutPacket writeHeartbeat() {
        return new OutPacket(OutCode.HEARTBEAT.value);
    }
    
    public static OutPacket writeHandshake(boolean connectionAllowed) {
        OutPacket packet = new OutPacket(OutCode.HANDSHAKE.value);
        
        packet.writeBoolean(connectionAllowed);
        packet.writeString(ServerConfig.SERVER_VERSION);
        packet.writeLong(Instant.now().getEpochSecond());
        
        return packet;
    }
    
    public static OutPacket writeMessage(short group, String message, String sender) {
        OutPacket packet = new OutPacket(OutCode.MESSAGE.value);
        
        packet.writeShort(group);
        packet.writeString(message);
        packet.writeString(sender);
        
        return packet;
    }
    
    public static OutPacket writeLoginFailure(int reason) {
        OutPacket packet = new OutPacket(OutCode.LOGIN_RESULT.value);
        
        packet.writeBoolean(false);
        packet.writeByte((byte) reason);
        
        return packet;
    }
    
    public static OutPacket writeLoginSuccess(PlayerCharacter pc) {
        OutPacket packet = new OutPacket(OutCode.LOGIN_RESULT.value);
        
        packet.writeBoolean(true);
        packet.writeString(pc.getUsername());
        
        Scene sc = pc.getScene();
        packet.writeInt(sc.getId());
        packet.writeInt(sc.getSpawnPoint().x);
        packet.writeInt(sc.getSpawnPoint().y);
        
        return packet;
    }
    
}
