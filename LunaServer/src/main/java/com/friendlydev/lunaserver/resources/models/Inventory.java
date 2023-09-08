package com.friendlydev.lunaserver.resources.models;

/**
 *
 * @author Marc
 */
public class Inventory {
    private PlayerCharacter owner;

    public Inventory(PlayerCharacter owner) {
        this.owner = owner;
    }

    public PlayerCharacter getOwner() {
        return owner;
    }
    
}
