package com.friendlydev.lunaserver.resources.models;

import com.friendlydev.lunaserver.database.DatabaseManager;
import com.friendlydev.lunaserver.packets.PacketWriter;
import com.friendlydev.lunaserver.resources.ResourceManager;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Marc
 */
public class Inventory {
    private static final Logger logger = LogManager.getLogger(Inventory.class);
    
    public static final int SIZE = 80;
    
    ResourceManager rm;
    private final PlayerCharacter owner;
    private ArrayList<InventoryItem> itemSlots;

    public Inventory(PlayerCharacter owner) {
        rm = ResourceManager.getInstance();
        this.owner = owner;
        
        itemSlots = new ArrayList<>();
        // Give the array list ab entry for each inventory slot
        for (int i=0; i<SIZE; i++) {
            itemSlots.add(null);
        }
    }

    public PlayerCharacter getOwner() {
        return owner;
    }
    
    public ArrayList<InventoryItem> getItemSlots() {
        return itemSlots;
    }
    
    public int addItem(Item item, int quantity) {
        int leftOverQuantity = quantity;
        int targetSlot;
        
        while (leftOverQuantity > 0) {
            targetSlot = getAvailableSlot(item);
            if (targetSlot == -1) break;
            
            leftOverQuantity = addToSlot(item, targetSlot, leftOverQuantity);
        }
        return leftOverQuantity;
    }
    
    public int addItem(int itemId, int quantity) {
        Item item = rm.getItem(itemId);
        if (item != null) {
            return addItem(item, quantity);
        } else {
            return quantity;
        }
    }
    
    public InventoryItem getItem(int slot) {
        try {
            return itemSlots.get(slot);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
    
    public int getFirstEmptySlot() {
        int currentSlot = 0;
        
        for (InventoryItem inventoryItem : itemSlots) {
            if (inventoryItem == null) return currentSlot;
            currentSlot += 1;
        }
        
        return -1;
    }
    
    public int getAvailableSlot(Item item) {
        int currentSlot = 0;
        
        for (InventoryItem inventoryItem : itemSlots) {
            if (inventoryItem == null) {
                currentSlot += 1;
                continue;
            }
            
            if (inventoryItem.getItemId() == item.getId()) {
                if (inventoryItem.getQuantity() < item.getMaxQuantity()) return currentSlot;
            }
            
            currentSlot += 1;
        }
        
        // If item doesn't exist just take the first empty slot
        return getFirstEmptySlot();
    }
    
    public int addToSlot(Item item, int slot, int quantity) {
        InventoryItem existingItem = getItem(slot);
        
        if (existingItem == null) {
            int leftOverQuantity = quantity - item.getMaxQuantity();
            if (leftOverQuantity > 0) {
                setSlot(item, slot, item.getMaxQuantity());
            } else {
                setSlot(item, slot, quantity);
            }
            // If leftOverQuantity is negative there are no leftover items. Return 0
            return Math.max(0, leftOverQuantity);
        }
        
        if (existingItem.getItemId() != item.getId()) {
            return quantity;
        }
        
        if (existingItem.getQuantity() == item.getMaxQuantity()) {
            return quantity;
        }
        
        // The items match up and quantity can be added
        int leftOverQuantity = existingItem.add(quantity);
        setSlot(existingItem, slot);
        
        return leftOverQuantity;
    }
    
    public void setSlot(Item item, int slot, int quantity) {
        // Change the item into an InventoryItem and pass it on
        InventoryItem inventoryItem = new InventoryItem(owner.getId(), slot, item, quantity);
        setSlot(inventoryItem, slot);
    }
    
    public void setSlot(InventoryItem inventoryItem, int slot) {
        // Update the position of the InventoryItem
        if (inventoryItem != null) {
            inventoryItem.setPosition(slot);
        }
        
        try {
            itemSlots.set(slot, inventoryItem);
            sendInventoryUpdate(slot);
        } catch (IndexOutOfBoundsException e) {
            logger.warn("Tried to move items out of inventory bounds");
        }
    }
    
    public void removeItem(int slot) {
        InventoryItem inventoryItem = getItem(slot);
        
        if (inventoryItem != null) {
            DatabaseManager.deleteFromDB(inventoryItem);
            setSlot(null, slot);
        }
    }
    
    public void swapSlots(int slot1, int slot2) {
        // Validate slot numbers. Slot 2 can be -1 to indicate dropping the item from slot 1
        if (slot1 < 0 || slot1 >= SIZE || slot2 >= SIZE) return;
        if (slot2 < 0 && slot2 != -1) return;
        
        InventoryItem tempItem1 = getItem(slot1);
        if (tempItem1 == null) return;
        
        // If slot 2 is -1 the item from slot 1 should be removed
        if (slot2 == -1) {
            removeItem(slot1);
            return;
        }
        
        InventoryItem tempItem2 = getItem(slot2);
        
        // If there's no item in slot 2 move item from slot 1 over
        if (tempItem2 == null) {
            setSlot(null, slot1);
            setSlot(tempItem1, slot2);
            return;
        }
        
        // If both slots contain the same item stack them together
        if (tempItem1.getItemId() == tempItem2.getItemId()) {
            int leftOverQuantity = addToSlot(tempItem1.getItem(), slot2, tempItem1.getQuantity());
            
            if (leftOverQuantity > 0) {
                tempItem1.setQuantity(leftOverQuantity);
                setSlot(tempItem1, slot1);
            } else {
                // If leftOverQuantity is negative the full stack has been moved over
                removeItem(slot1);
            }
            
            return;
        }
        
        // Swap the slots around
        setSlot(tempItem2, slot1);
        setSlot(tempItem1, slot2);
    }
    
    public ArrayList<InventoryItem> getItems() {
        ArrayList<InventoryItem> items = new ArrayList<>();
        
        for (InventoryItem itemSlot : itemSlots) {
            if (itemSlot == null) continue;
            
            items.add(itemSlot);
        }
        
        return items;
    }
    
    public boolean containsItem(int itemId) {
        for (InventoryItem inventoryItem : itemSlots) {
            if (inventoryItem == null) continue;
            
            if (inventoryItem.getItemId() == itemId) return true;
        }
        
        return false;
    }
    
    public boolean takeItemQuantity(int itemId, int quantity) {
        for (InventoryItem inventoryItem : itemSlots) {
            if (inventoryItem == null) continue;
            
            if (inventoryItem.getItemId() == itemId) {
                int slot = inventoryItem.getPosition();

                if (inventoryItem.getQuantity() >= quantity) {
                    int leftOverQuantity = inventoryItem.take(quantity);
                    if (leftOverQuantity <= 0) {
                        removeItem(slot);
                    }

                    sendInventoryUpdate(slot);
                    return true;
                }
            }
        }
        return false;
    }
    
    public void consumeItem(int slot) {
        InventoryItem inventoryItem = getItem(slot);
        
        if (inventoryItem == null) return;
        
        if (inventoryItem.getItem() instanceof ConsumableItem consumableItem) {
            if (inventoryItem.getQuantity() < 1) return;
            
            // Check if the item has been consumed
            if (consumableItem.consume(owner.getClientHandler())) {
                // If stack size reaches 0 remove the item from the inventory
                if (inventoryItem.take(1) <= 0) {
                    removeItem(slot);
                    return;
                }
                
                sendInventoryUpdate(slot);
            }
        }
    }
    
    public void sendInventoryUpdate(int slot) {
        InventoryItem inventoryItem = getItem(slot);
        
        if (inventoryItem == null) {
            // An empty inventory slot has itemId -1 and a quantity of 0
            owner.getClientHandler().sendPacket(PacketWriter.writeAlterInventory(slot, -1, 0));
        } else {
            owner.getClientHandler().sendPacket(PacketWriter.writeAlterInventory(slot, inventoryItem.getItemId(), inventoryItem.getQuantity()));
        }
    }
    
    public void save() {
        for (InventoryItem inventoryItem : itemSlots) {
            if (inventoryItem != null) {
                DatabaseManager.saveOrUpdateInDB(inventoryItem);
            }
        }
    }
    
}
