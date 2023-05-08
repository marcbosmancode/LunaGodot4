package com.friendlydev.lunaserver.scheduling;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 *
 * @author Marc
 */
public class FutureEventManager {
    // Singleton implementation
    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
    
    public static ScheduledExecutorService getExecutor() {
        return executor;
    }
    
}
