package com.friendlydev.lunaserver.login;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author Marc
 */
public class EncryptionService {
    public static byte[] generateSalt(){
        SecureRandom random;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
            // Generate a salt of 8 bytes
            byte[] salt = new byte[8];
            random.nextBytes(salt);
            return salt;
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }
    
    public static byte[] encryptPassword(String password, byte[] salt){
        try {
            // 1000 iterations, 160 derivedKeyLength
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 1000, 160);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            return null;
        }
    }
    
    public static boolean validatePassword(String password, byte[] encryptedPassword, byte[] salt) {
        // Encrypt the given password with the salt given and check if it equals the given encrypted password
        byte[] encryptedGivenPassword = encryptPassword(password, salt);
        return Arrays.equals(encryptedGivenPassword, encryptedPassword);
    }
    
}
