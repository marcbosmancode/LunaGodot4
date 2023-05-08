package com.friendlydev.lunaserver.resources;

import com.friendlydev.lunaserver.client.ClientHandler;
import com.friendlydev.lunaserver.login.AccountService;
import com.friendlydev.lunaserver.resources.models.Item;
import com.friendlydev.lunaserver.resources.models.Scene;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Marc
 */
public class ResourceManager {
    private static final Logger logger = LogManager.getLogger(ResourceManager.class);
    
    // Singleton implementation
    private static final ResourceManager instance = new ResourceManager();
    
    private ResourceManager() {}
    
    public static ResourceManager getInstance() {
        return instance;
    }
    
    private HashMap<Integer, Item> itemData;
    private HashMap<Integer, Scene> sceneData;
    
    private ArrayList<ClientHandler> activeClients = new ArrayList<>();
    
    public void init() throws IOException {
        itemData = ResourceLoader.loadItemJson();
        sceneData = ResourceLoader.loadSceneJson();
        
        // Create an admin account if one doesn't already exist
        if (AccountService.registerAccount("admin", "admin")) {
            logger.info("Creating admin account with username = admin password = admin");
        }
    }
    
    public Item getItem(int id) {
        return itemData.get(id);
    }
    
    public Scene getScene(int id) {
        return sceneData.get(id);
    }

    public ArrayList<ClientHandler> getAllClients() {
        return activeClients;
    }
    
    public void addClient(ClientHandler ch) {
        activeClients.add(ch);
    }
    
    public void removeClient(ClientHandler ch) {
        activeClients.remove(ch);
    }
    
}
