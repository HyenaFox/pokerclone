package com.example.gui;

import com.example.Card;
import com.example.Player;
import java.util.List;

/**
 * Implementation of a GUI-controlled player.
 * This player represents the human in the GUI version, with actions
 * selected through the GUI rather than console input.
 */
public class GUIPlayer implements Player {
    private String name;
    private int chipCount;
    
    /**
     * Creates a new GUI player.
     * 
     * @param name The player's name
     * @param initialChips The initial chip count
     */
    public GUIPlayer(String name, int initialChips) {
        this.name = name;
        this.chipCount = initialChips;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public int getChipCount() {
        return chipCount;
    }
    
    @Override
    public void addChips(int amount) {
        if (amount > 0) {
            chipCount += amount;
        }
    }
    
    @Override
    public boolean removeChips(int amount) {
        if (amount <= 0 || amount > chipCount) {
            return false;
        }
        chipCount -= amount;
        return true;
    }
    
    @Override
    public PlayerAction getAction(List<Card> hand, List<Card> communityCards, int currentBet, int potAmount) {
        // In the GUI implementation, we don't use this method directly
        // The action is determined by button clicks in the PokerGUI class
        // This is just a placeholder implementation
        return PlayerAction.CHECK;
    }
}
