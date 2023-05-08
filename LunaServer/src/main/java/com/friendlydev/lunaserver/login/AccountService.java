package com.friendlydev.lunaserver.login;

import com.friendlydev.lunaserver.database.DatabaseManager;
import com.friendlydev.lunaserver.resources.models.Account;
import java.util.Date;
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
        
        return true;
    }
    
    public static Account getAccount(String username) {
        String usernameLowercase = username.toLowerCase();
        
        return (Account) DatabaseManager.getFromDB(Account.class, "username", usernameLowercase);
    }
    
}
