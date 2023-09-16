package com.friendlydev.lunaserver.packets;

import com.friendlydev.lunaserver.chat.ChatCommandHandler;
import com.friendlydev.lunaserver.client.ClientHandler;
import com.friendlydev.lunaserver.constants.ServerConfig;
import com.friendlydev.lunaserver.constants.enums.PacketCodes.InCode;
import com.friendlydev.lunaserver.login.AccountService;
import com.friendlydev.lunaserver.resources.models.Account;
import com.friendlydev.lunaserver.resources.models.PlayerCharacter;
import java.io.EOFException;
import java.util.ArrayList;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Marc
 */
public class PacketHandler {
    private static final Logger logger = LogManager.getLogger(PacketHandler.class);
    
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
                    
                    // Prevent sending to yourself and notify if the message couldn't be sent
                    if (!messageTarget.equals(ch.getPlayerCharacter().getUsername().toLowerCase())) {
                        boolean result = ch.sendPacketToPlayer(PacketWriter.writeMessage(group, message, ch.getAccount().getUsername()), messageTarget);
                        if (result == false) {
                            ch.sendPacket(PacketWriter.writeMessage((short) 0, "Could not message player", "Server"));
                        }
                    }
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
                // Check if the account is banned
                if (acc.isBanned()) {
                    String banComment = acc.getBanReason();
                    Date unbanDate = acc.getUnbanDate();
                    if (banComment == null) {
                        ch.sendPacket(PacketWriter.writeLoginFailure(2));
                    } else {
                        ch.sendPacket(PacketWriter.writeLoginFailureBanned(banComment, unbanDate));
                    }
                    return;
                }
                
                // Check if the account is logged in
                if (acc.isLoggedIn()) {
                    ch.sendPacket(PacketWriter.writeLoginFailure(1));
                    return;
                }
                
                // Load player characters from the account and log in
                ArrayList<PlayerCharacter> accPlayerCharacters = AccountService.getAccountPlayerCharacters(acc.getId());
                if (accPlayerCharacters.size() >= 1) {
                    PlayerCharacter pc = accPlayerCharacters.get(0);
                    ch.login(acc, pc);
                    ch.sendPacket(PacketWriter.writeLoginSuccess(pc));
                    
                    return;
                }
            }
        }
        
        ch.sendPacket(PacketWriter.writeLoginFailure(0));
    }
    
}
