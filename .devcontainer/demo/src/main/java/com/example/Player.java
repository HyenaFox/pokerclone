package com.example;

import java.util.List;

/**
 * Interface representing a poker player.
 */
public interface Player {
    /**
     * Gets the player's name.
     * 
     * @return The player's name
     */
    String getName();
    
    /**
     * Gets the player's current chip count.
     * 
     * @return The player's chip count
     */
    int getChipCount();
    
    /**
     * Adds chips to the player's stack.
     * 
     * @param amount The amount of chips to add
     */
    void addChips(int amount);
    
    /**
     * Removes chips from the player's stack.
     * 
     * @param amount The amount of chips to remove
     * @return True if the player had enough chips, false otherwise
     */
    boolean removeChips(int amount);
    
    /**
     * Player decides what action to take.
     * 
     * @param hand The player's current hand
     * @param communityCards The community cards on the table
     * @param currentBet The current bet amount to call
     * @param potAmount The current pot amount
     * @return The player's decision
     */
    PlayerAction getAction(List<Card> hand, List<Card> communityCards, int currentBet, int potAmount);
    
    /**
     * Enum representing possible player actions in poker.
     */
    enum PlayerAction {
        FOLD,
        CHECK,
        CALL,
        RAISE,
        ALL_IN
    }
}
