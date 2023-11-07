package com.friendlydev.lunaserver.constants.enums;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Marc
 */
public class ItemConstants {
    public enum ItemType {
        ETC(0),
        CONSUMABLE(1),
        CURRENCY(2),
        WEAPON(3),
        ARMOR(4),
        ACCESSORY(5);
        
        public final int value;
        
        private ItemType(int value) {
            this.value = value;
        }
        
        private static final Map<Integer, ItemType> ENUM_MAP;
        static {
            Map<Integer, ItemType> map = new ConcurrentHashMap<>();
            for (ItemType type : ItemType.values()) {
                map.put(type.value, type);
            }
            ENUM_MAP = Collections.unmodifiableMap(map);
        }
        
        public static ItemType get(int value) {
            return ENUM_MAP.get(value);
        }
    }
}
