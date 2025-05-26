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
        // Analyze hand strength (0-10 scale)
        int handStrength = evaluateHandStrength(hand, communityCards);
        
        // Calculate pot odds and implied odds
        double potOdds = (double)currentBet / (potAmount + currentBet);
        
        // Check if we have enough chips to make the bet
        if (currentBet > chipCount) {
            // Can only fold or go all-in
            if (handStrength >= 5) {
                return PlayerAction.ALL_IN; // Good enough hand to go all-in
            } else {
                return PlayerAction.FOLD;   // Not worth the all-in
            }
        }
        
        // Betting strategy based on game stage and hand strength
        boolean preFlop = communityCards.isEmpty();
        boolean earlyGame = chipCount > 500; // More than half of starting chips
        
        // Pre-flop strategy
        if (preFlop) {
            if (handStrength >= 9) {
                // Premium hand: raise or go all-in to build pot
                return random.nextDouble() < 0.7 ? PlayerAction.RAISE : PlayerAction.ALL_IN;
            } else if (handStrength >= 7) {
                // Strong starting hand: raise or call
                if (currentBet == 0) {
                    return random.nextDouble() < 0.8 ? PlayerAction.RAISE : PlayerAction.CHECK;
                } else {
                    return random.nextDouble() < 0.6 ? PlayerAction.RAISE : PlayerAction.CALL;
                }
            } else if (handStrength >= 4) {
                // Playable hand: call or check, occasionally raise
                if (currentBet == 0) {
                    return random.nextDouble() < 0.2 ? PlayerAction.RAISE : PlayerAction.CHECK;
                } else {
                    // Only call if the bet is reasonable
                    if (potOdds < 0.25) {
                        return PlayerAction.CALL;
                    } else {
                        return PlayerAction.FOLD;
                    }
                }
            } else {
                // Weak hand: check if possible, otherwise fold
                if (currentBet == 0) {
                    return PlayerAction.CHECK;
                } else {
                    return PlayerAction.FOLD;
                }
            }
        }
        // Post-flop strategy
        else {
            // Made hand (very strong)
            if (handStrength >= 8) {
                // Slow play 20% of the time to trap opponents
                if (random.nextDouble() < 0.2 && currentBet == 0) {
                    return PlayerAction.CHECK;
                } else if (random.nextDouble() < 0.4) {
                    return PlayerAction.RAISE;
                } else {
                    return PlayerAction.ALL_IN;
                }
            } 
            // Strong hand
            else if (handStrength >= 6) {
                if (currentBet == 0) {
                    return random.nextDouble() < 0.8 ? PlayerAction.RAISE : PlayerAction.CHECK;
                } else {
                    return random.nextDouble() < 0.4 ? PlayerAction.RAISE : PlayerAction.CALL;
                }
            } 
            // Medium hand
            else if (handStrength >= 4) {
                if (currentBet == 0) {
                    return random.nextDouble() < 0.4 ? PlayerAction.RAISE : PlayerAction.CHECK;
                } else {
                    // Call if the pot odds are good
                    if (potOdds < 0.3) {
                        return PlayerAction.CALL;
                    } else {
                        return random.nextDouble() < 0.3 ? PlayerAction.CALL : PlayerAction.FOLD;
                    }
                }
            } 
            // Weak hand
            else {
                // Occasionally bluff with weak hands
                if (random.nextDouble() < 0.05) {
                    return PlayerAction.RAISE; // Bluff!
                } else if (currentBet == 0) {
                    return PlayerAction.CHECK;
                } else {
                    return PlayerAction.FOLD;
                }
            }
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
