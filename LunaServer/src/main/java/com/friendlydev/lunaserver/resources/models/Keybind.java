package com.friendlydev.lunaserver.resources.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 *
 * @author Marc
 */
@Entity
@IdClass(KeybindId.class)
@Table(name = "keybinds")
public class Keybind {
    @Id
    @Column(name = "characterid")
    private int characterId;
    @Id
    @Column(name = "hotkeyid")
    private int hotkeyId;
    @Column(name = "actiontype")
    private int actionType;
    @Column(name = "actionid")
    private int actionId;
    
    // Empty constructor for hibernate
    public Keybind() {
        
    }

    public Keybind(int characterId, int hotkeyId, int actionType, int actionId) {
        this.characterId = characterId;
        this.hotkeyId = hotkeyId;
        this.actionType = actionType;
        this.actionId = actionId;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public int getHotkeyId() {
        return hotkeyId;
    }

    public void setHotkeyId(int hotkeydId) {
        this.hotkeyId = hotkeyId;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }
}
