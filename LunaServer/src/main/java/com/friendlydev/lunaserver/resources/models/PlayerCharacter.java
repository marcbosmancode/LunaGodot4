package com.friendlydev.lunaserver.resources.models;

import com.friendlydev.lunaserver.client.ClientHandler;
import com.friendlydev.lunaserver.resources.ResourceManager;
import java.awt.Point;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Marc
 */
@Entity
@Table(name = "players")
public class PlayerCharacter {
    @Transient
    private static final Logger logger = LogManager.getLogger(PlayerCharacter.class);
    @Transient
    private ClientHandler handler;
    @Transient
    ResourceManager rm = ResourceManager.getInstance();
    
    // Base information
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "accountid")
    private int accountId;
    @Column(name = "username")
    private String username;
    @Column(name = "sceneid")
    private int sceneId; // Used to save scene reference to the database
    @Transient
    private Scene scene;
    @Transient
    private Point position = new Point();
    @Transient
    private Inventory inventory;
    @Transient
    private ClientHandler ch;
    
    // Combat stats
    @Column(name = "combatlevel")
    private int level;
    @Column(name = "combatexp")
    private int exp;
    @Column(name = "maxhealth")
    private int maxHealth;
    @Column(name = "health")
    private int health;
    @Column(name = "attack")
    private int attack;
    
    // Default constructor for hibernate
    public PlayerCharacter() {
        inventory = new Inventory(this);
    }

    public PlayerCharacter(int accountId, String username) {
        // Set base values for a newly created player
        this(accountId, username, 1, 1, 0, 100, 100, 1);
    }

    public PlayerCharacter(int accountId, String username, int sceneId, int level, int exp, int maxHealth, int health, int attack) {
        inventory = new Inventory(this);
        this.accountId = accountId;
        this.username = username;
        this.sceneId = sceneId;
        scene = rm.getScene(sceneId);
        this.level = level;
        this.exp = exp;
        this.maxHealth = maxHealth;
        this.health = health;
        this.attack = attack;
    }

    public ClientHandler getHandler() {
        return handler;
    }

    public void setHandler(ClientHandler handler) {
        this.handler = handler;
    }

    public int getId() {
        return id;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public int getSceneId() {
        return sceneId;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        sceneId = scene.getId();
        this.scene = scene;
    }
    
    public void setScene(int sceneId) {
        this.sceneId = sceneId;
        scene = rm.getScene(sceneId);
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position.setLocation(position);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
    
    public ClientHandler getClientHandler() {
        return ch;
    }
    
    public void setClientHandler(ClientHandler ch) {
        this.ch = ch;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }
    
}
