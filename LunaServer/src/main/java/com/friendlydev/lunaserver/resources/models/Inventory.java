package com.friendlydev.lunaserver.resources.models;

/**
 *
 * @author Marc
 */
public class Inventory {
    private Player owner;

    public Inventory(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }
    
}
