package com.example;

import java.util.List;
import java.util.Scanner;

/**
 * Implementation of a human-controlled Player.
 */
public class HumanPlayer implements Player {
    private String name;
    private int chipCount;
    private Scanner scanner;
    
    /**
     * Creates a new human player with a given name and chip count.
     * 
     * @param name The player's name
     * @param initialChips The player's initial chip count
     */
    public HumanPlayer(String name, int initialChips) {
        this.name = name;
        this.chipCount = initialChips;
        this.scanner = new Scanner(System.in);
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
        // Display relevant information to the player
        System.out.println("\n--- " + name + "'s turn ---");
        System.out.println("Your chips: " + chipCount);
        System.out.println("Current pot: " + potAmount);
        System.out.println("Current bet to call: " + currentBet);
        
        // Show player's cards
        System.out.println("\nYour hand:");
        for (Card card : hand) {
            System.out.println("  " + card.toString());
        }
        
        // Show community cards if any
        if (!communityCards.isEmpty()) {
            System.out.println("\nCommunity cards:");
            for (Card card : communityCards) {
                System.out.println("  " + card.toString());
            }
        }
        
        // Get player's action
        System.out.println("\nChoose your action:");
        System.out.println("1: Fold");
        System.out.println("2: Check/Call");
        System.out.println("3: Raise");
        System.out.println("4: All-in");
        
        int choice;
        do {
            System.out.print("Enter your choice (1-4): ");
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Enter a number (1-4): ");
                scanner.next();
            }
            choice = scanner.nextInt();
        } while (choice < 1 || choice > 4);
        
        // Return the appropriate action
        switch (choice) {
            case 1:
                return PlayerAction.FOLD;
            case 2:
                return currentBet == 0 ? PlayerAction.CHECK : PlayerAction.CALL;
            case 3:
                return PlayerAction.RAISE;
            case 4:
                return PlayerAction.ALL_IN;
            default:
                return PlayerAction.FOLD;
        }
    }
}
