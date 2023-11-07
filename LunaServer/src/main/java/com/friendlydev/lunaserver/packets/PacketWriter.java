package com.friendlydev.lunaserver.packets;

import com.friendlydev.lunaserver.constants.ServerConfig;
import com.friendlydev.lunaserver.constants.enums.PacketCodes.OutCode;
import com.friendlydev.lunaserver.resources.models.PlayerCharacter;
import com.friendlydev.lunaserver.resources.models.Scene;
import java.awt.Point;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

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
        
        /*
        0: Invalid credentials
        1: Already online
        2: Banned
        */
        packet.writeByte((byte) reason);
        
        return packet;
    }
    
    public static OutPacket writeLoginFailureBanned(String comment, Date unbanDate) {
        OutPacket packet = new OutPacket(OutCode.LOGIN_RESULT.value);
        
        String banComment = "";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String dateString = dateFormat.format(unbanDate);
        
        if (comment != null) {
            banComment += ". Ban reason: " + comment;
        }
        if (unbanDate != null) {
            banComment += ". Unban date: " + dateString;
        }
        
        packet.writeBoolean(false);
        packet.writeByte((byte) 3);
        packet.writeString(banComment);
        
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
    
    public static OutPacket writePlayerPositionUpdate(PlayerCharacter pc, boolean usedTeleport) {
        OutPacket packet = new OutPacket(OutCode.PLAYER_POSITION_UPDATE.value);
        
        packet.writeInt(pc.getId());
        packet.writePoint(pc.getPosition());
        packet.writeBoolean(usedTeleport);
        
        return packet;
    }
    
    public static OutPacket writePlayerStateUpdate(PlayerCharacter pc, String newAnimation, int newDirection) {
        OutPacket packet = new OutPacket(OutCode.PLAYER_STATE_UPDATE.value);
        
        packet.writeInt(pc.getId());
        packet.writeString(newAnimation);
        packet.writeInt(newDirection);
        
        return packet;
    }
    
    public static OutPacket writeChangeScenePacket(Scene sc, Point targetPosition) {
        OutPacket packet = new OutPacket(OutCode.CHANGE_SCENE.value);
        
        packet.writeInt(sc.getId());
        packet.writePoint(targetPosition);
        
        return packet;
    }
    
    public static OutPacket writePlayerEnteredScenePacket(PlayerCharacter pc) {
        OutPacket packet = new OutPacket(OutCode.PLAYER_ENTERED_SCENE.value);
        
        packet.writeInt(pc.getId());
        packet.writeString(pc.getUsername());
        packet.writePoint(pc.getPosition());
        
        return packet;
    }
    
    public static OutPacket writePlayerLeftScenePacket(PlayerCharacter pc) {
        OutPacket packet = new OutPacket(OutCode.PLAYER_LEFT_SCENE.value);
        
        packet.writeInt(pc.getId());
        
        return packet;
    }
    
    public static OutPacket writeAlterInventory(int slot, int itemId, int quantity) {
        OutPacket packet = new OutPacket(OutCode.ALTER_INVENTORY.value);
        
        packet.writeInt(slot);
        packet.writeInt(itemId);
        packet.writeInt(quantity);        
        
        return packet;
    }
    
}
