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
        
        // Get player's action with appropriate options based on the situation
        System.out.println("\nChoose your action:");
        
        // Determine available actions based on betting situation
        boolean canCheck = (currentBet == 0);
        
        // Show available actions
        System.out.println("1: Fold");
        
        if (canCheck) {
            System.out.println("2: Check");
        } else {
            System.out.println("2: Call " + currentBet + " chips");
        }
        
        // Show raise option if player has enough chips
        if (chipCount > currentBet + 1) {
            System.out.println("3: Raise");
        }
        
        System.out.println("4: All-in (" + chipCount + " chips)");
        
        int choice;
        do {
            System.out.print("Enter your choice: ");
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Enter a number: ");
                scanner.next();
            }
            choice = scanner.nextInt();
            
            // Validate choice based on available options
            if (choice == 3 && chipCount <= currentBet + 1) {
                System.out.println("You don't have enough chips to raise.");
                choice = 0; // Invalid choice
            }
            
        } while (choice < 1 || choice > 4);
        
        // Return the appropriate action
        switch (choice) {
            case 1:
                return PlayerAction.FOLD;
            case 2:
                return canCheck ? PlayerAction.CHECK : PlayerAction.CALL;
            case 3:
                return PlayerAction.RAISE;
            case 4:
                return PlayerAction.ALL_IN;
            default:
                return PlayerAction.FOLD;
        }
    }
}
