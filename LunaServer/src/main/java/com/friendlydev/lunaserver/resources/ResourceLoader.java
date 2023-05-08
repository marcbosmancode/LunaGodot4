package com.friendlydev.lunaserver.resources;

import com.friendlydev.lunaserver.constants.enums.ItemConstants.ItemType;
import com.friendlydev.lunaserver.resources.models.ConsumableItem;
import com.friendlydev.lunaserver.resources.models.Item;
import com.friendlydev.lunaserver.resources.models.Scene;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Marc
 */
public class ResourceLoader {
    private static final Logger logger = LogManager.getLogger(ResourceLoader.class);
    private static final String RESOURCES_PATH = "src/main/resources/";
    
    public static HashMap<Integer, Item> loadItemJson() throws IOException {
        HashMap<Integer, Item> itemData = new HashMap<>();
        String filePath = RESOURCES_PATH + "item_data.json";
        
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                
                int itemType = jsonObj.getInt("type");
                Item item;
                
                if (itemType == ItemType.CONSUMABLE.value) {
                    // Load optional parameters from the consumable data
                    JSONObject consumableData = jsonObj.getJSONObject("consumabledata");
                    int healing = 0;
                    if (consumableData.has("healing")) {
                        consumableData.getInt("healing");
                    }
                    String script = "";
                    if (consumableData.has("script")) {
                        consumableData.getString("script");
                    }
                    
                    item = new ConsumableItem(
                            healing,
                            script,
                            jsonObj.getInt("id"),
                            jsonObj.getInt("type"),
                            jsonObj.getString("name"),
                            jsonObj.getString("tooltip"),
                            jsonObj.getInt("price"),
                            jsonObj.getInt("maxquantity"),
                            jsonObj.getBoolean("tradelocked")
                    );
                } else {
                    item = new Item(
                            jsonObj.getInt("id"),
                            jsonObj.getInt("type"),
                            jsonObj.getString("name"),
                            jsonObj.getString("tooltip"),
                            jsonObj.getInt("price"),
                            jsonObj.getInt("maxquantity"),
                            jsonObj.getBoolean("tradelocked")
                    );
                }
                itemData.put(item.getId(), item);
            }
            logger.info("Loaded " + jsonArray.length() + " items from item_data.json");
        } catch (NoSuchFileException ex) {
            logger.error("Failed loading item_data.json");
        }
        return itemData;
    }
    
    public static HashMap<Integer, Scene> loadSceneJson() throws IOException {
        HashMap<Integer, Scene> sceneData = new HashMap<>();
        String filePath = RESOURCES_PATH + "scene_data.json";
        
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                
                Scene scene = new Scene(
                        jsonObj.getInt("id")
                );
                sceneData.put(scene.getId(), scene);
            }
            logger.info("Loaded " + jsonArray.length() + " scenes from scene_data.json");
        } catch (NoSuchFileException ex) {
            logger.error("Failed loading scene_data.json");
        }
        return sceneData;
    }
}
