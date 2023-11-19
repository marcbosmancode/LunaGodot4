package com.friendlydev.lunaserver.scripts;

import com.friendlydev.lunaserver.client.ClientHandler;
import com.friendlydev.lunaserver.constants.enums.ScriptTypes.ScriptType;
import com.friendlydev.lunaserver.packets.PacketWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Marc
 */
public class PythonScriptHandler {
    private static final Logger logger = LogManager.getLogger(PythonScriptHandler.class);
    
    private static final String SCRIPT_PATH = "src/main/resources/scripts/";
    private static final String FILE_EXTENSION = ".py";
    
    private ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("python");
    private ClientHandler ch;
    
    public PythonScriptHandler(ClientHandler ch) {
        this.ch = ch;
    }
    
    public void runScript(String fileName, ScriptType scriptType) {
        String filePathString = SCRIPT_PATH + scriptType.getDirectory() + fileName + FILE_EXTENSION;
        
        // Check if the script exists
        boolean fileExists = new File(filePathString).exists();
        if (!fileExists) {
            ch.sendPacket(PacketWriter.writeMessage((short) 0, "Interaction script failed to load", "Server"));
            return;
        }
        
        try {
            Bindings bindings = new SimpleBindings();
            bindings.put("sh", this);
            
            String scriptContent = Files.readString(Path.of(filePathString));
            
            scriptEngine.eval(scriptContent, bindings);
        } catch (Exception ex) {
            logger.warn("Failed running script: " + filePathString);
            ex.printStackTrace();
        }
    }
    
    public void sendMessage(String message) {
        ch.sendPacket(PacketWriter.writeMessage((short) 0, message, "Server"));
    }
    
    public int getReturnScene() {
        return ch.getPlayerCharacter().getScene().getReturnScene();
    }
    
    public void changeScene(int sceneId) {
        ch.getPlayerCharacter().changeScene(sceneId);
    }
    
    public void takeItem(int itemId, int quantity) {
        ch.getPlayerCharacter().getInventory().takeItemQuantity(itemId, quantity);
    }
    
    public void giveItem(int itemId, int quantity) {
        ch.getPlayerCharacter().getInventory().addItem(itemId, quantity);
    }
    
}
