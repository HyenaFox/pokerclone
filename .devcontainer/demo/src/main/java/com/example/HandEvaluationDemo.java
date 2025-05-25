package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A demo class to showcase the poker hand evaluation system.
 */
public class HandEvaluationDemo {
    
    public static void main(String[] args) {
        System.out.println("Poker Hand Evaluation Demo");
        System.out.println("==========================\n");
        
        GameEngine engine = new GameEngine();
        
        // Demo all hand types
        demonstrateRoyalFlush(engine);
        demonstrateStraightFlush(engine);
        demonstrateFourOfAKind(engine);
        demonstrateFullHouse(engine);
        demonstrateFlush(engine);
        demonstrateStraight(engine);
        demonstrateThreeOfAKind(engine);
        demonstrateTwoPair(engine);
        demonstrateOnePair(engine);
        demonstrateHighCard(engine);
    }
    
    private static void demonstrateRoyalFlush(GameEngine engine) {
        List<Card> playerCards = new ArrayList<>(Arrays.asList(
            new Card(1, Suit.Hearts),  // Ace of Hearts
            new Card(13, Suit.Hearts)  // King of Hearts
        ));
        
        List<Card> communityCards = new ArrayList<>(Arrays.asList(
            new Card(12, Suit.Hearts),  // Queen of Hearts
            new Card(11, Suit.Hearts),  // Jack of Hearts
            new Card(10, Suit.Hearts),  // 10 of Hearts
            new Card(9, Suit.Clubs),    // 9 of Clubs
            new Card(8, Suit.Diamonds)  // 8 of Diamonds
        ));
        
        evaluateAndPrintHand("Royal Flush", engine, playerCards, communityCards);
    }
    
    private static void demonstrateStraightFlush(GameEngine engine) {
        List<Card> playerCards = new ArrayList<>(Arrays.asList(
            new Card(9, Suit.Spades),   // 9 of Spades
            new Card(8, Suit.Spades)    // 8 of Spades
        ));
        
        List<Card> communityCards = new ArrayList<>(Arrays.asList(
            new Card(7, Suit.Spades),   // 7 of Spades
            new Card(6, Suit.Spades),   // 6 of Spades
            new Card(5, Suit.Spades),   // 5 of Spades
            new Card(4, Suit.Hearts),   // 4 of Hearts
            new Card(3, Suit.Diamonds)  // 3 of Diamonds
        ));
        
        evaluateAndPrintHand("Straight Flush", engine, playerCards, communityCards);
    }
    
    private static void demonstrateFourOfAKind(GameEngine engine) {
        List<Card> playerCards = new ArrayList<>(Arrays.asList(
            new Card(7, Suit.Hearts),   // 7 of Hearts
            new Card(7, Suit.Spades)    // 7 of Spades
        ));
        
        List<Card> communityCards = new ArrayList<>(Arrays.asList(
            new Card(7, Suit.Diamonds), // 7 of Diamonds
            new Card(7, Suit.Clubs),    // 7 of Clubs
            new Card(5, Suit.Spades),   // 5 of Spades
            new Card(4, Suit.Hearts),   // 4 of Hearts
            new Card(3, Suit.Diamonds)  // 3 of Diamonds
        ));
        
        evaluateAndPrintHand("Four of a Kind", engine, playerCards, communityCards);
    }
    
    private static void demonstrateFullHouse(GameEngine engine) {
        List<Card> playerCards = new ArrayList<>(Arrays.asList(
            new Card(10, Suit.Hearts),  // 10 of Hearts
            new Card(10, Suit.Spades)   // 10 of Spades
        ));
        
        List<Card> communityCards = new ArrayList<>(Arrays.asList(
            new Card(10, Suit.Diamonds), // 10 of Diamonds
            new Card(5, Suit.Clubs),     // 5 of Clubs
            new Card(5, Suit.Spades),    // 5 of Spades
            new Card(4, Suit.Hearts),    // 4 of Hearts
            new Card(3, Suit.Diamonds)   // 3 of Diamonds
        ));
        
        evaluateAndPrintHand("Full House", engine, playerCards, communityCards);
    }
    
    private static void demonstrateFlush(GameEngine engine) {
        List<Card> playerCards = new ArrayList<>(Arrays.asList(
            new Card(10, Suit.Clubs),  // 10 of Clubs
            new Card(8, Suit.Clubs)    // 8 of Clubs
        ));
        
        List<Card> communityCards = new ArrayList<>(Arrays.asList(
            new Card(6, Suit.Clubs),   // 6 of Clubs
            new Card(4, Suit.Clubs),   // 4 of Clubs
            new Card(2, Suit.Clubs),   // 2 of Clubs
            new Card(9, Suit.Hearts),  // 9 of Hearts
            new Card(7, Suit.Diamonds) // 7 of Diamonds
        ));
        
        evaluateAndPrintHand("Flush", engine, playerCards, communityCards);
    }
    
    private static void demonstrateStraight(GameEngine engine) {
        List<Card> playerCards = new ArrayList<>(Arrays.asList(
            new Card(9, Suit.Hearts),   // 9 of Hearts
            new Card(8, Suit.Spades)    // 8 of Spades
        ));
        
        List<Card> communityCards = new ArrayList<>(Arrays.asList(
            new Card(7, Suit.Diamonds), // 7 of Diamonds
            new Card(6, Suit.Clubs),    // 6 of Clubs
            new Card(5, Suit.Spades),   // 5 of Spades
            new Card(4, Suit.Hearts),   // 4 of Hearts
            new Card(3, Suit.Diamonds)  // 3 of Diamonds
        ));
        
        evaluateAndPrintHand("Straight", engine, playerCards, communityCards);
    }
    
    private static void demonstrateThreeOfAKind(GameEngine engine) {
        List<Card> playerCards = new ArrayList<>(Arrays.asList(
            new Card(8, Suit.Hearts),   // 8 of Hearts
            new Card(8, Suit.Spades)    // 8 of Spades
        ));
        
        List<Card> communityCards = new ArrayList<>(Arrays.asList(
            new Card(8, Suit.Diamonds), // 8 of Diamonds
            new Card(10, Suit.Clubs),   // 10 of Clubs
            new Card(5, Suit.Spades),   // 5 of Spades
            new Card(4, Suit.Hearts),   // 4 of Hearts
            new Card(3, Suit.Diamonds)  // 3 of Diamonds
        ));
        
        evaluateAndPrintHand("Three of a Kind", engine, playerCards, communityCards);
    }
    
    private static void demonstrateTwoPair(GameEngine engine) {
        List<Card> playerCards = new ArrayList<>(Arrays.asList(
            new Card(9, Suit.Hearts),   // 9 of Hearts
            new Card(9, Suit.Spades)    // 9 of Spades
        ));
        
        List<Card> communityCards = new ArrayList<>(Arrays.asList(
            new Card(7, Suit.Diamonds), // 7 of Diamonds
            new Card(7, Suit.Clubs),    // 7 of Clubs
            new Card(5, Suit.Spades),   // 5 of Spades
            new Card(4, Suit.Hearts),   // 4 of Hearts
            new Card(3, Suit.Diamonds)  // 3 of Diamonds
        ));
        
        evaluateAndPrintHand("Two Pair", engine, playerCards, communityCards);
    }
    
    private static void demonstrateOnePair(GameEngine engine) {
        List<Card> playerCards = new ArrayList<>(Arrays.asList(
            new Card(10, Suit.Hearts),  // 10 of Hearts
            new Card(10, Suit.Spades)   // 10 of Spades
        ));
        
        List<Card> communityCards = new ArrayList<>(Arrays.asList(
            new Card(8, Suit.Diamonds), // 8 of Diamonds
            new Card(7, Suit.Clubs),    // 7 of Clubs
            new Card(5, Suit.Spades),   // 5 of Spades
            new Card(4, Suit.Hearts),   // 4 of Hearts
            new Card(3, Suit.Diamonds)  // 3 of Diamonds
        ));
        
        evaluateAndPrintHand("One Pair", engine, playerCards, communityCards);
    }
    
    private static void demonstrateHighCard(GameEngine engine) {
        List<Card> playerCards = new ArrayList<>(Arrays.asList(
            new Card(1, Suit.Hearts),   // Ace of Hearts
            new Card(10, Suit.Spades)   // 10 of Spades
        ));
        
        List<Card> communityCards = new ArrayList<>(Arrays.asList(
            new Card(8, Suit.Diamonds), // 8 of Diamonds
            new Card(7, Suit.Clubs),    // 7 of Clubs
            new Card(5, Suit.Spades),   // 5 of Spades
            new Card(4, Suit.Hearts),   // 4 of Hearts
            new Card(3, Suit.Diamonds)  // 3 of Diamonds
        ));
        
        evaluateAndPrintHand("High Card (Ace)", engine, playerCards, communityCards);
    }
    
    private static void evaluateAndPrintHand(String expectedHand, GameEngine engine, 
                                          List<Card> playerCards, List<Card> communityCards) {
        System.out.println("Expected hand: " + expectedHand);
        System.out.println("Player cards: " + formatCards(playerCards));
        System.out.println("Community cards: " + formatCards(communityCards));
        
        int score = engine.calculateHandScoreForAI(playerCards, communityCards);
        GameEngine.HandType handType = engine.determineHandType(score);
        
        System.out.println("Evaluated as: " + handType.getDisplayName());
        System.out.println("Score: " + score);
        System.out.println();
    }
    
    private static String formatCards(List<Card> cards) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cards.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(cards.get(i));
        }
        return sb.toString();
    }
}
