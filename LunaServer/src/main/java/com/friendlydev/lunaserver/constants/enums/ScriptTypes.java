package com.friendlydev.lunaserver.constants.enums;

/**
 *
 * @author Marc
 */
public class ScriptTypes {
    public enum ScriptType {
        ITEM("item/"),
        NPC("npc/");
        
        private String directory;
        
        private ScriptType(String directory) {
            this.directory = directory;
        }
        
        public String getDirectory() {
            return directory;
        }
    }
}
