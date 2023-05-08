package com.friendlydev.lunaserver.resources.models;

/**
 *
 * @author Marc
 */
public class Item {
    private final int id;
    private final int type;
    private final String name;
    private final String tooltip;
    private final int price;
    private final int maxQuantity;
    private final boolean tradeLocked;

    public Item(int id, int type, String name, String tooltip, int price, int maxQuantity, boolean tradeLocked) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.tooltip = tooltip;
        this.price = price;
        this.maxQuantity = maxQuantity;
        this.tradeLocked = tradeLocked;
    }

    public int getId() {
        return id;
    }
    
    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getTooltip() {
        return tooltip;
    }

    public int getPrice() {
        return price;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public boolean isTradeLocked() {
        return tradeLocked;
    }
    
}
