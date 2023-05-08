package com.friendlydev.lunaserver.client;

import com.friendlydev.lunaserver.packets.InPacket;
import com.friendlydev.lunaserver.packets.OutPacket;
import com.friendlydev.lunaserver.packets.PacketHandler;
import com.friendlydev.lunaserver.packets.PacketWriter;
import com.friendlydev.lunaserver.resources.ResourceManager;
import com.friendlydev.lunaserver.resources.models.Account;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Marc
 */
public class ClientHandler implements Runnable {
    private static final Logger logger = LogManager.getLogger(ClientHandler.class);
    
    private final Socket client;
    private boolean running = false;
    private DataInputStream in;
    private DataOutputStream out;
    private ResourceManager rm;
    
    private boolean loggedIn;
    private Account account;
    
    private static final int CONNECTION_TIMEOUT_MS = 1000 * 30; // 30 seconds
    
    public ClientHandler(Socket client) throws IOException {
        this.client = client;
        in = new DataInputStream(client.getInputStream());
        out = new DataOutputStream(client.getOutputStream());
        rm = ResourceManager.getInstance();
    }
    
    @Override
    public void run() {
        logger.info("New client " + client.getInetAddress() + " connected. Running client handler");
        running = true;
        
        try (client) {
            // Set a timeout to validate if the connection is being kept alive with the heartbeat
            client.setSoTimeout(CONNECTION_TIMEOUT_MS);
            
            // Send a handshake packet to notify the client about the established connection
            sendPacket(PacketWriter.writeHandshake(true));
            
            while(running) {
                // Read the packet size which is indicated as an integer (little endian)
                int byte1 = in.read();
                int byte2 = in.read();
                int byte3 = in.read();
                int byte4 = in.read();
                
                if ((byte1 | byte2 | byte3 | byte4) < 0) {
                    throw new EOFException();
                }
                int packetSize = (byte1 + (byte2 << 8) + (byte3 << 16) + (byte4 << 24));
                
                // Read the next bytes from the incoming packet
                byte[] packetData = new byte[packetSize];
                
                for (int i = 0; i < packetSize; i++) {
                    int data = in.read();
                    if (data < 0) {
                        throw new EOFException();
                    }
                    packetData[i] = (byte) data;
                }
                InPacket inPacket = new InPacket(packetData);
                logger.info("Received a packet from client with code " + inPacket.getPacketCode());
                
                PacketHandler.handlePacket(this, inPacket);
            }
        } catch (IOException ex) {
            // Will be triggered when something goes wrong or the client closes the connection
            logger.info("Connection with client " + client.getInetAddress() + " got lost");
            rm.removeClient(this);
        }
    }
    
    public void sendPacket(OutPacket packet) {
        try {
            logger.info("Sending packet with code " + packet.getPacketCode());
            out.write(packet.getPacketData());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void sendPacketToAllOthers(OutPacket packet) {
        for (ClientHandler ch : rm.getAllClients()) {
            if (ch != this) {
                ch.sendPacket(packet);
            }
        }
    }
    
    public void sendPacketToAll(OutPacket packet) {
        for (ClientHandler ch : rm.getAllClients()) {
            ch.sendPacket(packet);
        }
    }
    
    public void sendPacketToPlayer(OutPacket packet, String username) {
        for (ClientHandler ch : rm.getAllClients()) {
            if (ch.isLoggedIn()) {
                if (ch.getAccount().getUsername().equals(username)) {
                    ch.sendPacket(packet);
                }
            }
        }
    }
    
    public boolean isLoggedIn() {
        return loggedIn;
    }
    
    public Account getAccount() {
        return account;
    }
    
    public void login(Account account) {
        loggedIn = true;
        this.account = account;
    }
    
}
