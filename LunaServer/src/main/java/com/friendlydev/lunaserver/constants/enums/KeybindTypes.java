package com.friendlydev.lunaserver.constants.enums;

/**
 *
 * @author Marc
 */
public class KeybindTypes {
    public enum KeybindType {
        EMPTY(-1),
        ITEM(0),
        SKILL(1);
        
        public final int value;
        
        private KeybindType(int value) {
            this.value = value;
        }
    }
}
