package com.friendlydev.lunaserver.resources.models;

import com.friendlydev.lunaserver.database.DatabaseManager;
import com.friendlydev.lunaserver.packets.PacketWriter;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Marc
 */
public class PlayerSettings {
    private static final Logger logger = LogManager.getLogger(Inventory.class);
    
    public static final int HOTBAR_SIZE = 9;
    
    private final PlayerCharacter owner;
    private ArrayList<Keybind> keybinds;

    public PlayerSettings(PlayerCharacter owner) {
        this.owner = owner;
        
        keybinds = new ArrayList<>();
        // Give the array list an entry for each keybind
        for (int i=0; i<HOTBAR_SIZE; i++) {
            keybinds.add(null);
        }
    }
    
    public PlayerCharacter getOwner() {
        return owner;
    }
    
    public ArrayList<Keybind> getKeybinds() {
        return keybinds;
    }
    
    public void setKeybind(Keybind keybind, boolean updateClient) {
        try {
            keybinds.set(keybind.getHotkeyId() - 1, keybind);
            
            // No need to update the player when they are the one requesting the hotkey change
            if (updateClient) {
                sendKeybindUpdate(keybind);
            }
        } catch (IndexOutOfBoundsException e) {
            logger.warn("Tried to add a keybind on a nonexisting hotkey");
        }
    }
    
    public void sendKeybindUpdate(Keybind keybind) {
        owner.getClientHandler().sendPacket(PacketWriter.writeUpdateKeybind(keybind));
    }
    
    public void save() {
        for (Keybind keybind : keybinds) {
            if (keybind != null) {
                DatabaseManager.saveOrUpdateInDB(keybind);
            }
        }
    }
    
}
