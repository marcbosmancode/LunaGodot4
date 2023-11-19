package com.friendlydev.lunaserver.resources.models;

import com.friendlydev.lunaserver.client.ClientHandler;
import com.friendlydev.lunaserver.constants.enums.ScriptTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Marc
 */
public class ConsumableItem extends Item {
    private static final Logger logger = LogManager.getLogger(ConsumableItem.class);
    
    private int healing;
    private String script;
    
    public ConsumableItem(int healing, String script, int id, int type, String name, String tooltip, int price, int maxQuantity, boolean tradeLocked) {
        super(id, type, name, tooltip, price, maxQuantity, tradeLocked);
        this.healing = healing;
        this.script = script;
    }
    
    /**
     * Consume the item and run the respective script
     * @param ch the client that consumes this item
     * @return if the item is consumed
     */
    public boolean consume(ClientHandler ch) {
        // TODO add healing
        
        if (script != null) {
            ch.getScriptHandler().runScript(script, ScriptTypes.ScriptType.ITEM);
            
            // By default let the script handle item consumption
            return false;
        }
        
        return true;
    }
    
}
