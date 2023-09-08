package com.friendlydev.lunaserver.resources.models;

import java.awt.Point;

/**
 *
 * @author Marc
 */
public class NonPlayerCharacter {
    private int id;
    private String name;
    private Point position;

    public NonPlayerCharacter(int id, String name, Point position) {
        this.id = id;
        this.name = name;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }
    
}
