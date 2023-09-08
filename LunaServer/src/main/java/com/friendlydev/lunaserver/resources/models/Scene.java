package com.friendlydev.lunaserver.resources.models;

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
    private ArrayList<Door> allDoors = new ArrayList<>();;
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

    public ArrayList<PlayerCharacter> getAllPlayers() {
        return allPlayers;
    }

    public void setAllPlayers(ArrayList<PlayerCharacter> allPlayers) {
        this.allPlayers = allPlayers;
    }
    
    public void addPlayer(PlayerCharacter player) {
        allPlayers.add(player);
    }
    
    public void removePlayer(PlayerCharacter player) {
        allPlayers.remove(player);
    }
    
}
