package com.friendlydev.lunaserver.constants.enums;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Marc
 */
public class PacketCodes {
    public enum InCode {
        HEARTBEAT((short) 0),
        MESSAGE((short) 1),
        LOGIN((short) 2),
        PLAYER_POSITION_UPDATE((short) 3),
        PLAYER_STATE_UPDATE((short) 4),
        DOOR_INTERACTION((short) 5);
        
        public final short value;
        
        private InCode(short value) {
            this.value = value;
        }
        
        private static final Map<Short, InCode> ENUM_MAP;
        static {
            Map<Short, InCode> map = new ConcurrentHashMap<>();
            for (InCode code : InCode.values()) {
                map.put(code.value, code);
            }
            ENUM_MAP = Collections.unmodifiableMap(map);
        }
        
        public static InCode get(short value) {
            return ENUM_MAP.get(value);
        }
    }
    
    public enum OutCode {
        HEARTBEAT((short) 0),
        HANDSHAKE((short) 1),
        MESSAGE((short) 2),
        LOGIN_RESULT((short) 3),
        PLAYER_POSITION_UPDATE((short) 4),
        PLAYER_STATE_UPDATE((short) 5),
        CHANGE_SCENE((short) 6),
        PLAYER_ENTERED_SCENE((short) 7),
        PLAYER_LEFT_SCENE((short) 8);
        
        public final short value;
        
        private OutCode(short value) {
            this.value = value;
        }
    }
}
