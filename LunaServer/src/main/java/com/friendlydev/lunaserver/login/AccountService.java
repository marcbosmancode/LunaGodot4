package com.friendlydev.lunaserver.login;

import com.friendlydev.lunaserver.database.DatabaseManager;
import com.friendlydev.lunaserver.resources.models.Account;
import com.friendlydev.lunaserver.resources.models.PlayerCharacter;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Marc
 */
public class AccountService {
    private static final Logger logger = LogManager.getLogger(AccountService.class);
    
    public static boolean registerAccount(String username, String password) {
        String usernameLowercase = username.toLowerCase();
        
        // Check if the account already exists
        Account acc = (Account) DatabaseManager.getFromDB(Account.class, "username", usernameLowercase);
        if (acc != null) return false;
        
        // Encrypt the password and save the new account to the database
        byte[] salt = EncryptionService.generateSalt();
        byte[] encryptedPassword = EncryptionService.encryptPassword(password, salt);
        
        Account newAccount = new Account(usernameLowercase, encryptedPassword, salt);
        DatabaseManager.saveInDB(newAccount);
        
        // For now create a player character together with the account
        createPlayerCharacter(newAccount.getId(), username);
        
        logger.info("New account with username= " + username + " registered");
        
        return true;
    }
    
    public static Account getAccount(String username) {
        return (Account) DatabaseManager.getFromDB(Account.class, "username", username);
    }
    
    public static boolean createPlayerCharacter(int accountId, String username) {
        // Check if the character already exists
        PlayerCharacter pc = (PlayerCharacter) DatabaseManager.getFromDB(PlayerCharacter.class, "username", username);
        if (pc != null) return false;
        
        PlayerCharacter newPlayerCharacter = new PlayerCharacter(accountId, username);
        DatabaseManager.saveInDB(newPlayerCharacter);
        
        logger.info("New character with username=" + username);
        
        return true;
    }
    
    public static ArrayList<PlayerCharacter> getAccountPlayerCharacters(int accountId) {
        ArrayList<PlayerCharacter> accPlayerCharacters = (ArrayList<PlayerCharacter>) DatabaseManager.getListFromDB(PlayerCharacter.class, "accountid", accountId);
        
        // Database just saves scene id. Load an actual reference to the scene
        for (PlayerCharacter pc : accPlayerCharacters) {
            pc.setScene(pc.getSceneId());
        }
        
        return accPlayerCharacters;
    }
    
}
