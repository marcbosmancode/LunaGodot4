package com.friendlydev.lunaserver.resources;

import com.friendlydev.lunaserver.constants.enums.ItemConstants.ItemType;
import com.friendlydev.lunaserver.resources.models.ConsumableItem;
import com.friendlydev.lunaserver.resources.models.Door;
import com.friendlydev.lunaserver.resources.models.Item;
import com.friendlydev.lunaserver.resources.models.NonPlayerCharacter;
import com.friendlydev.lunaserver.resources.models.Scene;
import java.awt.Point;
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
                        healing = consumableData.getInt("healing");
                    }
                    boolean proportionalHealing = false;
                    if (consumableData.has("proportionalhealing")) {
                        proportionalHealing = consumableData.getBoolean("proportionalhealing");
                    }
                    String script = null;
                    if (consumableData.has("script")) {
                        script = consumableData.getString("script");
                    }
                    
                    item = new ConsumableItem(
                            healing,
                            proportionalHealing,
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
                
                JSONObject spawnPointData = jsonObj.getJSONObject("spawnpoint");
                Point spawnPoint = new Point(spawnPointData.getInt("x"), spawnPointData.getInt("y"));
                
                Scene scene = new Scene(
                        jsonObj.getInt("id"),
                        jsonObj.getString("name"),
                        spawnPoint,
                        jsonObj.getInt("returnscene")
                );
                
                // Add additional data to the scene
                JSONArray doorsData = jsonObj.getJSONArray("doors");
                for (int j = 0; j < doorsData.length(); j++) {
                    JSONObject doorJsonObj = doorsData.getJSONObject(j);
                    
                    JSONObject positionData = doorJsonObj.getJSONObject("position");
                    Point position = new Point(positionData.getInt("x"), positionData.getInt("y"));
                    
                    JSONObject destinationData = doorJsonObj.getJSONObject("destinationpoint");
                    Point destinationPoint = new Point(destinationData.getInt("x"), destinationData.getInt("y"));
                    
                    Door door = new Door(
                            doorJsonObj.getInt("id"),
                            position,
                            doorJsonObj.getInt("destination"),
                            destinationPoint
                    );
                    scene.addDoor(door);
                }
                
                JSONArray npcData = jsonObj.getJSONArray("npcs");
                for (int k = 0; k < npcData.length(); k++) {
                    JSONObject npcJsonObj = npcData.getJSONObject(k);
                    
                    JSONObject positionData = npcJsonObj.getJSONObject("position");
                    Point position = new Point(positionData.getInt("x"), positionData.getInt("y"));
                    
                    NonPlayerCharacter npc = new NonPlayerCharacter(
                            npcJsonObj.getInt("id"),
                            npcJsonObj.getString("name"),
                            position
                    );
                    scene.addNPC(npc);
                }
                
                sceneData.put(scene.getId(), scene);
            }
            logger.info("Loaded " + jsonArray.length() + " scenes from scene_data.json");
        } catch (NoSuchFileException ex) {
            logger.error("Failed loading scene_data.json");
        }
        return sceneData;
    }
}
