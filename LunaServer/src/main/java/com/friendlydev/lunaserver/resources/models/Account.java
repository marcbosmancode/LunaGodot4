package com.friendlydev.lunaserver.resources.models;

import com.friendlydev.lunaserver.database.DatabaseManager;
import com.friendlydev.lunaserver.login.EncryptionService;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.CreationTimestamp;

/**
 *
 * @author Marc
 */
@Entity
@Table(name = "accounts")
public class Account {
    @Transient
    private static final Logger logger = LogManager.getLogger(Account.class);
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private byte[] password;
    @Column(name = "salt")
    private byte[] salt;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creationdate")
    private Date creationDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lastlogin")
    private Date lastLogin;
    @Column(name = "admin")
    private boolean admin = false;
    @Column(name = "banned")
    private boolean banned = false;
    @Column(name = "banreason")
    private String banReason;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "unbandate")
    private Date unbanDate;
    
    @Transient
    private boolean loggedIn = false;
    
    // Default constructor for hibernate
    public Account() {}
    
    public Account(String username, byte[] encryptedPassword, byte[] salt) {
        this.username = username;
        password = encryptedPassword;
        this.salt = salt;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getPassword() {
        return password;
    }

    public byte[] getSalt() {
        return salt;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isBanned() {
        if (banned == true) {
            // If no unban date is available the ban is permanent
            if (unbanDate == null) return true;
            
            // Check for ban expiration
            Date currentTime = new Date();
            if (currentTime.after(unbanDate)) {
                unban();
                return false;
            }
        }
        
        return false;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public String getBanReason() {
        return banReason;
    }

    public void setBanReason(String banReason) {
        this.banReason = banReason;
    }

    public Date getUnbanDate() {
        return unbanDate;
    }

    public void setUnbanDate(Date unbanDate) {
        this.unbanDate = unbanDate;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
    
    public boolean changePassword(String currentPassword, String newPassword) {
        // First validate the current password
        if (EncryptionService.validatePassword(currentPassword, password, salt)) {
            // Encrypt the new password and save it
            byte[] newSalt = EncryptionService.generateSalt();
            byte[] newEncryptedPassword = EncryptionService.encryptPassword(newPassword, newSalt);
            
            salt = newSalt;
            password = newEncryptedPassword;
            DatabaseManager.updateInDB(this);
            
            return true;
        }
        return false;
    }
    
    public boolean authenticate(String givenPassword) {
        boolean result = EncryptionService.validatePassword(givenPassword, password, salt);
        
        // Update login timestamp on successful login
        if (result) {
            lastLogin = new Date();
            DatabaseManager.updateInDB(this);
        }
        
        return result;
    }
    
    public void unban() {
        banned = false;
        DatabaseManager.updateInDB(this);
    }
    
    public void ban(int durationDays, String reason) {
        banned = true;
        banReason = reason;
        
        // Calculate what time the account will be unbanned
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, durationDays);
        unbanDate = c.getTime();
        
        DatabaseManager.updateInDB(this);
    }
    
    // Permanent ban
    public void ban(String reason) {
        banned = true;
        banReason = reason;
        unbanDate = null;
        
        DatabaseManager.updateInDB(this);
    }
    
}
