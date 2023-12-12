package com.friendlydev.lunaserver.constants.enums;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Marc
 */
public class StatConstants {
    public enum VitalType {
        HEALTH(0),
        MANA(1),
        EXPERIENCE(2);
        
        public final int value;
        
        private VitalType(int value) {
            this.value = value;
        }
        
        private static final Map<Integer, VitalType> ENUM_MAP;
        static {
            Map<Integer, VitalType> map = new ConcurrentHashMap<>();
            for (VitalType type : VitalType.values()) {
                map.put(type.value, type);
            }
            ENUM_MAP = Collections.unmodifiableMap(map);
        }
        
        public static VitalType get(int value) {
            return ENUM_MAP.get(value);
        }
    }
    
    public enum StatType {
        MAX_HEALTH(0),
        MAN_MANA(1),
        LEVEL(2),
        ATTACK(3);
        
        public final int value;
        
        private StatType(int value) {
            this.value = value;
        }
        
        private static final Map<Integer, StatType> ENUM_MAP;
        static {
            Map<Integer, StatType> map = new ConcurrentHashMap<>();
            for (StatType type : StatType.values()) {
                map.put(type.value, type);
            }
            ENUM_MAP = Collections.unmodifiableMap(map);
        }
        
        public static StatType get(int value) {
            return ENUM_MAP.get(value);
        }
    }
}
