package com.friendlydev.lunaserver.resources.models;

import com.friendlydev.lunaserver.packets.OutPacket;
import com.friendlydev.lunaserver.packets.PacketWriter;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Marc
 */
public class Scene {
    private int id;
    private String name;
    private Point spawnPoint;
    private int returnScene;
    private ArrayList<Door> allDoors = new ArrayList<>();
    private ArrayList<NonPlayerCharacter> allNPCs = new ArrayList<>();
    private ArrayList<PlayerCharacter> allPlayers = new ArrayList<>();

    public Scene(int id, String name, Point spawnPoint, int returnScene) {
        this.id = id;
        this.name = name;
        this.spawnPoint = spawnPoint;
        this.returnScene = returnScene;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Point getSpawnPoint() {
        return spawnPoint;
    }

    public int getReturnScene() {
        return returnScene;
    }

    public ArrayList<NonPlayerCharacter> getAllNPCs() {
        return allNPCs;
    }

    public void setAllNPCs(ArrayList<NonPlayerCharacter> allNPCs) {
        this.allNPCs = allNPCs;
    }
    
    public void addNPC(NonPlayerCharacter npc) {
        allNPCs.add(npc);
    }
    
    public void removeNPC(NonPlayerCharacter npc) {
        allNPCs.remove(npc);
    }

    public ArrayList<Door> getAllDoors() {
        return allDoors;
    }

    public void setAllDoors(ArrayList<Door> allDoors) {
        this.allDoors = allDoors;
    }
    
    public void addDoor(Door door) {
        allDoors.add(door);
    }
    
    public void removeDoor(Door door) {
        allDoors.remove(door);
    }
    
    public Door getDoor(int id) {
        for (Door door : allDoors) {
            if (door.getId() == id) return door;
        }
        return null;
    }

    public ArrayList<PlayerCharacter> getAllPlayers() {
        return allPlayers;
    }
    
    /**
     * Add a PlayerCharacter to the Scene and notify all others in the Scene
     * 
     * @param player PlayerCharacter to be added
     */
    public void addPlayer(PlayerCharacter player) {
        // Notify other players a player entered
        OutPacket playerEnteredScenePacket = PacketWriter.writePlayerEnteredScenePacket(player);
        sendPacketToAll(playerEnteredScenePacket);
        
        // Send all other players in the scene to the player
        for (PlayerCharacter pc : allPlayers) {
            OutPacket otherPlayerDataPacket = PacketWriter.writePlayerEnteredScenePacket(pc);
            if (player.getClientHandler() != null) {
                player.getClientHandler().sendPacket(otherPlayerDataPacket);
            }
        }
        
        allPlayers.add(player);
    }
    
    /**
     * Remove a PlayerCharacter from the Scene and notify all others in the Scene
     * 
     * @param player PlayerCharacter to be removed
     */
    public void removePlayer(PlayerCharacter player) {
        allPlayers.remove(player);
        
        // Notify other players a player leaves
        OutPacket playerLeftScenePacket = PacketWriter.writePlayerLeftScenePacket(player);
        sendPacketToAll(playerLeftScenePacket);
    }
    
    public void sendPacketToAll(OutPacket packet) {
        for (PlayerCharacter pc : allPlayers) {
            if (pc.getClientHandler() != null) {
                pc.getClientHandler().sendPacket(packet);
            }
        }
    }
    
}
