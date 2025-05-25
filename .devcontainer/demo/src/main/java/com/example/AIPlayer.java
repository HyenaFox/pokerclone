package com.example;

import java.util.List;
import java.util.Random;

/**
 * Implementation of an AI-controlled Player.
 * This AI makes decisions based on basic poker strategy.
 */
public class AIPlayer implements Player {
    private String name;
    private int chipCount;
    private Random random;
    
    /**
     * Creates a new AI player with a given name and chip count.
     * 
     * @param name The AI player's name
     * @param initialChips The AI player's initial chip count
     */
    public AIPlayer(String name, int initialChips) {
        this.name = name;
        this.chipCount = initialChips;
        this.random = new Random();
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
        // Analyze hand strength (simplified version)
        int handStrength = evaluateHandStrength(hand, communityCards);
        
        // Base decision on hand strength and other factors
        if (handStrength > 8) {
            // Strong hand - raise or go all-in
            return random.nextBoolean() ? PlayerAction.RAISE : PlayerAction.ALL_IN;
        } else if (handStrength > 5) {
            // Decent hand - call or raise
            int decision = random.nextInt(3);
            switch (decision) {
                case 0: return PlayerAction.CALL;
                case 1: return PlayerAction.RAISE;
                default: return PlayerAction.CHECK;
            }
        } else if (handStrength > 3) {
            // Weak hand - check or call
            return random.nextBoolean() ? PlayerAction.CHECK : PlayerAction.CALL;
        } else {
            // Very weak hand - fold or check
            return random.nextInt(10) < 7 ? PlayerAction.FOLD : PlayerAction.CHECK;
        }
    }
    
    /**
     * Evaluates the strength of a poker hand for AI decision making.
     * Uses the GameEngine's evaluation logic and adapts it to a 0-10 scale.
     * 
     * @param hand The player's private cards
     * @param communityCards The community cards
     * @return A value from 0 to 10 indicating hand strength (higher is better)
     */
    private int evaluateHandStrength(List<Card> hand, List<Card> communityCards) {
        // Create a temporary GameEngine to use its hand evaluation logic
        GameEngine tempEngine = new GameEngine();
        
        // Use the proper hand evaluation from GameEngine
        int handScore = tempEngine.calculateHandScoreForAI(hand, communityCards);
        
        // Convert the score to a 0-10 scale based on hand type
        if (handScore >= 9000000) {
            return 10; // Royal Flush
        } else if (handScore >= 8000000) {
            return 9;  // Straight Flush
        } else if (handScore >= 7000000) {
            return 8;  // Four of a Kind
        } else if (handScore >= 6000000) {
            return 7;  // Full House
        } else if (handScore >= 5000000) {
            return 6;  // Flush
        } else if (handScore >= 4000000) {
            return 5;  // Straight
        } else if (handScore >= 3000000) {
            return 4;  // Three of a Kind
        } else if (handScore >= 2000000) {
            return 3;  // Two Pair
        } else if (handScore >= 1000000) {
            return 2;  // One Pair
        } else {
            // High Card - scale from 0-1 based on the high card
            return handScore > 0 ? 1 : 0;
        }
    }
}
