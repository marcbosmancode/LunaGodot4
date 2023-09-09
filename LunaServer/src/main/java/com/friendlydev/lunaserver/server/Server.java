package com.friendlydev.lunaserver.server;

import com.friendlydev.lunaserver.client.ClientHandler;
import com.friendlydev.lunaserver.constants.ServerConfig;
import com.friendlydev.lunaserver.database.DatabaseManager;
import com.friendlydev.lunaserver.login.AccountService;
import com.friendlydev.lunaserver.packets.OutPacket;
import com.friendlydev.lunaserver.packets.PacketWriter;
import com.friendlydev.lunaserver.resources.ResourceManager;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Marc
 */
public class Server implements Runnable {
    private static final Logger logger = LogManager.getLogger(Server.class);
    
    private ServerSocket serverSocket;
    private Socket client;
    private boolean running = false;
    private ResourceManager rm = ResourceManager.getInstance();
    
    // Create a thread executor service for the maximum possible connections
    BlockingQueue<Runnable> queue = new SynchronousQueue<>();
    private final ExecutorService executor = new ThreadPoolExecutor(ServerConfig.MAX_PLAYERS, ServerConfig.MAX_PLAYERS, 0L, TimeUnit.MILLISECONDS, queue);

    @Override
    public void run() {
        try {
            launchServer();
        } catch (IOException ex) {
            logger.error("Server could not be launched. Is port " + ServerConfig.SERVER_PORT + " already in use?");
            ex.printStackTrace();
        }
    }
    
    /**
     * Loads all required game data into memory and starts the server
     */
    public void launchServer() throws IOException {
        logger.info("Starting " + ServerConfig.SERVER_NAME + " version " + ServerConfig.SERVER_VERSION);
        long startTime = System.currentTimeMillis();
        
        serverSocket = new ServerSocket(ServerConfig.SERVER_PORT);
        
        DatabaseManager.init();
        rm.init();
        
        running = true;
        logger.info("Server loaded in " + (System.currentTimeMillis() - startTime) + " ms. Running on port " + ServerConfig.SERVER_PORT);
        
        // Listen for clients connecting
        while(running) {
            client = serverSocket.accept();
            
            try {
                // Try to run the client on the thread pool executor
                ClientHandler clientThread = new ClientHandler(client);
                executor.execute(clientThread);
                
                rm.addClient(clientThread);
            } catch (RejectedExecutionException ex) {
                // Connection refused if server is full or if executor is stopped
                logger.warn("Client tried to connect but server has reached capacity");
                
                // Notify client about the server being full
                OutPacket packet = PacketWriter.writeHandshake(false);
                DataOutputStream out = new DataOutputStream(client.getOutputStream());
                try {
                    logger.info("Sending packet with code " + packet.getPacketCode());
                    out.write(packet.getPacketData());
                } catch (IOException ex2) {
                    ex2.printStackTrace();
                }
                
                client.close();
            }
        }
    }
    
}
