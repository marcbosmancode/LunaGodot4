package com.friendlydev.lunaserver.chat;

import com.friendlydev.lunaserver.client.ClientHandler;
import com.friendlydev.lunaserver.packets.PacketWriter;
import com.friendlydev.lunaserver.random.RandomGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Marc
 */
public class ChatCommandHandler {
    private static final Logger logger = LogManager.getLogger(ChatCommandHandler.class);
    
    public static String[] splitString(String s) {
        if (s.length() == 0) return null;
        String[] result = s.split(" ");
        return result;
    }
    
    public static void handleChatCommand(String commandMessage, ClientHandler ch) {
        String[] messageArray = splitString(commandMessage);
        if (messageArray == null) return;
        String givenCommand = messageArray[0].substring(1);
        
        String message = "";
        switch(givenCommand) {
            case "roll":
                message = ch.getPlayerCharacter().getUsername() + " rolled a " + RandomGenerator.getNumber(1, 100);
                ch.getPlayerCharacter().getScene().sendPacketToAll(PacketWriter.writeMessage((short) 0, message, "Server"));
                break;
        }
    }
}
