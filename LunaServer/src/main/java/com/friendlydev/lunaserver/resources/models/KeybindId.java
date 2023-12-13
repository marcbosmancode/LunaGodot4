package com.friendlydev.lunaserver.resources.models;

import java.io.Serializable;

/**
 *
 * @author Marc
 */
public class KeybindId implements Serializable {
    private int characterId;
    private int hotkeyId;
    
    // Empty constructor for hibernate
    public KeybindId() {
        
    }
    
    public KeybindId(int characterId, int hotkeyId) {
        this.characterId = characterId;
        this.hotkeyId = hotkeyId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final KeybindId other = (KeybindId) obj;
        if (this.characterId != other.characterId) {
            return false;
        }
        return this.hotkeyId == other.hotkeyId;
    }
    
}
