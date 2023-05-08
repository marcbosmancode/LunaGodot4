package com.friendlydev.lunaserver.resources.models;

/**
 *
 * @author Marc
 */
public class ConsumableItem extends Item {
    private int healing;
    private String script;

    public ConsumableItem(int healing, String script, int id, int type, String name, String tooltip, int price, int maxQuantity, boolean tradeLocked) {
        super(id, type, name, tooltip, price, maxQuantity, tradeLocked);
        this.healing = healing;
        this.script = script;
    }
    
    public void consume() {
        // TODO
    }
    
}
