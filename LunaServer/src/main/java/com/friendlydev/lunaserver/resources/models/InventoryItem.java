package com.friendlydev.lunaserver.resources.models;

import com.friendlydev.lunaserver.resources.ResourceManager;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Marc
 */
@Entity
@Access(AccessType.FIELD) // Using AccessType property on a variable requires the default AccessType to be declared
@Table(name = "inventoryitems")
public class InventoryItem {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "characterid")
    private int characterId;
    @Column(name = "itemid")
    private int itemId;
    @Column(name = "position")
    private int position;
    @Column(name = "quantity")
    private int quantity;
    @Transient
    private Item item;
    @Transient
    private ResourceManager rm = ResourceManager.getInstance();
    
    public InventoryItem() {
    }
    
    public InventoryItem(int characterId, int position, Item item, int quantity) {
        this.characterId = characterId;
        this.position = position;
        setItem(item);
        this.quantity = quantity;
    }
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public int getCharacterId() {
        return characterId;
    }
    
    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }
    
    // AccessType property will make database access use getters and setters for this variable
    @Access(AccessType.PROPERTY)
    public int getItemId() {
        return itemId;
    }
    
    public void setItemId(int itemId) {
        this.itemId = itemId;
        item = rm.getItem(itemId);
    }
    
    public int getPosition() {
        return position;
    }
    
    public void setPosition(int position) {
        this.position = position;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public Item getItem() {
        return item;
    }
    
    public void setItem(Item item) {
        this.item = item;
        itemId = item.getId();
    }
    
    /**
     * Add to the quantity of the item stack
     * @param amount amount to add
     * @return remaining quantity that couldn't be added
     */
    public int add(int amount) {
        if (amount <= 0) return 0;
        
        quantity += amount;
        
        int leftOverAmount = quantity - item.getMaxQuantity();
        if (leftOverAmount > 0) {
            quantity = item.getMaxQuantity();
        }
        
        // If leftOverAmount is negative there are no leftover items. So return 0
        return Math.max(0, leftOverAmount);
    }
    
    /**
     * Take from the quantity of the item stack
     * @param amount amount to be taken away
     * @return new quantity of the item stack
     */
    public int take(int amount) {
        quantity -= amount;
        
        // Prevent quantity from going negative
        quantity = Math.max(quantity, 0);
        
        return quantity;
    }
    
}
