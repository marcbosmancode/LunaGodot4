package com.friendlydev.lunaserver.random;

import java.util.Random;

/**
 *
 * @author Marc
 */
public class RandomGenerator {
    private static Random rand = new Random();
    
    /**
     * This will simulate if an event with a given chance succeeds.
     * Takes in a double that should be between 0.0 and 1.0
     * 
     * @param successChance Double between 0.0 and 1.0 indicating success chance
     * @return Boolean indicating the success
     */
    public static boolean isSuccess(double successChance) {
        double d = rand.nextDouble(); // Random number between 0.0 (Inclusive) and 1.0 (Exclusive)
        return d < successChance;
    }
    
    /**
     * This will simulate if an event with a given percentage chance succeeds. 
     * Takes an Integer that should be between 0 and 100
     * 
     * @param successChance Percentage chance between 0 and 100
     * @return Boolean indicating the success
     */
    public static boolean isSuccessPercentage(int successChance) {
        int i = rand.nextInt(100); // Random number between 0 (inclusive) and 100 (exclusive)
        return i < successChance;
    }
    
    /**
     * Generates a random number between two values. The given numbers are included
     * 
     * @param lowest Lowest possible number
     * @param highest Highest possible number
     * @return A number between (but including) two values
     */
    public static int getNumber(int lowest, int highest) {
        int difference = highest - lowest;
        int i = rand.nextInt(difference + 1); // Random number between 0 (inclusive) and difference (inclusive)
        
        return lowest + i;
    }
    
}
