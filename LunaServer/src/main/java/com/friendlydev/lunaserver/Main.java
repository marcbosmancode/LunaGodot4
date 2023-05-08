
package com.friendlydev.lunaserver;

import com.friendlydev.lunaserver.server.Server;

/**
 *
 * @author Marc
 */
public class Main {

    public static void main(String[] args) {
        Thread serverThread = new Thread(new Server());
        serverThread.start();
    }
}
