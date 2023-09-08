package com.friendlydev.lunaserver.resources.models;

import java.awt.Point;

/**
 *
 * @author Marc
 */
public class Door {
    private int id;
    private Point position;
    private int destination;
    private Point destinationPoint;

    public Door(int id, Point position, int destination, Point destinationPoint) {
        this.id = id;
        this.position = position;
        this.destination = destination;
        this.destinationPoint = destinationPoint;
    }

    public int getId() {
        return id;
    }

    public Point getPosition() {
        return position;
    }

    public int getDestination() {
        return destination;
    }

    public Point getDestinationPoint() {
        return destinationPoint;
    }
    
}
