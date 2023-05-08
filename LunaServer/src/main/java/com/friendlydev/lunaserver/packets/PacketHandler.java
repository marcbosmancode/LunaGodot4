package com.friendlydev.lunaserver.packets;

import com.friendlydev.lunaserver.chat.ChatCommandHandler;
import com.friendlydev.lunaserver.client.ClientHandler;
import com.friendlydev.lunaserver.constants.ServerConfig;
import com.friendlydev.lunaserver.constants.enums.PacketCodes.InCode;
import com.friendlydev.lunaserver.login.AccountService;
import com.friendlydev.lunaserver.resources.models.Account;
import java.io.EOFException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Marc
 */
public class PacketHandler {
    private static final Logger logger = LogManager.getLogger(PacketHandler.class);
    
    short code = InCode.HEARTBEAT.value;
    
    public static void handlePacket(ClientHandler ch, InPacket packet) throws EOFException {
        InCode packetCode = InCode.get(packet.getPacketCode());
        if (packetCode == null) {
            logger.warn("Given packet command is unknown (" + packet.getPacketCode() + ")");
            return;
        }
        
        switch (packetCode) {
            case HEARTBEAT:
                handleHeartbeat(ch, packet);
                break;
            case MESSAGE:
                handleMessage(ch, packet);
                break;
            case LOGIN:
                handleLogin(ch, packet);
                break;
        }
    }
    
    public static void handleHeartbeat(ClientHandler ch, InPacket packet) {
        // Send a heartbeat packet back in response
        ch.sendPacket(PacketWriter.writeHeartbeat());
    }
    
    public static void handleMessage(ClientHandler ch, InPacket packet) throws EOFException {
        // Make sure the account is logged in to send messages
        if (ch.isLoggedIn() == false) return;
        
        short group = packet.readShort();
        String message = packet.readString();
        
        // Check if the message contains a command
        char prefix = message.charAt(0);
        if (prefix == ServerConfig.COMMAND_CHAR) {
            ChatCommandHandler.handleChatCommand(message, ch);
        } else {
            switch(group) {
                case 1:
                    // To nearby players
                    ch.sendPacketToAllOthers(PacketWriter.writeMessage(group, message, ch.getAccount().getUsername()));
                    break;
                case 2:
                    // To world
                    ch.sendPacketToAllOthers(PacketWriter.writeMessage(group, message, ch.getAccount().getUsername()));
                    break;
                case 3:
                    // Private message
                    String messageTarget = packet.readString().toLowerCase();
                    ch.sendPacketToPlayer(PacketWriter.writeMessage(group, message, ch.getAccount().getUsername()), messageTarget);
                    break;
            }
        }
    }
    
    public static void handleLogin(ClientHandler ch, InPacket packet) throws EOFException {
        String username = packet.readString();
        String password = packet.readString();
        
        Account acc = AccountService.getAccount(username);
        if (acc != null) {
            if (acc.authenticate(password)) {
                ch.login(acc);
                ch.sendPacket(PacketWriter.writeLoginSuccess(acc.getUsername()));
                return;
            }
        }
        
        ch.sendPacket(PacketWriter.writeLoginFailure(0));
    }
    
}
