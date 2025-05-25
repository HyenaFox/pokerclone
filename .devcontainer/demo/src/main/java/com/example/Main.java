package com.example;

import com.example.Player.PlayerAction;
import java.util.List;
import java.util.Scanner;

/**
 * Main class to demonstrate the poker game.
 */
public class Main {
    private static GameEngine gameEngine;
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("Welcome to Poker Clone!");
        
        // Initialize the game engine with default blinds
        gameEngine = new GameEngine();
        
        // Add some players
        addPlayers();
        
        // Start a round
        if (gameEngine.startNewRound()) {
            System.out.println("New round started!");
            playRound();
        } else {
            System.out.println("Failed to start new round. Need at least 2 players.");
        }
    }
    
    /**
     * Add players to the game.
     */
    private static void addPlayers() {
        System.out.println("\nGame setup:");
        System.out.println("1: Play against AI");
        System.out.println("2: Two human players");
        
        int choice;
        do {
            System.out.print("Enter your choice (1-2): ");
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Enter a number (1-2): ");
                scanner.next();
            }
            choice = scanner.nextInt();
        } while (choice < 1 || choice > 2);
        
        // Default amount of chips
        final int STARTING_CHIPS = 1000;
        
        if (choice == 1) {
            // Human vs AI setup
            System.out.print("Enter your name: ");
            scanner.nextLine(); // Consume the newline
            String playerName = scanner.nextLine();
            
            Player humanPlayer = new HumanPlayer(playerName, STARTING_CHIPS);
            Player aiPlayer = new AIPlayer("AI Opponent", STARTING_CHIPS);
            
            gameEngine.addPlayer(humanPlayer);
            gameEngine.addPlayer(aiPlayer);
            
            System.out.println("Game setup complete: " + playerName + " vs AI Opponent");
            System.out.println("Each player starts with " + STARTING_CHIPS + " chips.");
            
            // Explain Texas Hold'em format
            explainPokerRules();
        } else {
            // Two human players setup
            System.out.print("Enter Player 1 name: ");
            scanner.nextLine(); // Consume the newline
            String player1Name = scanner.nextLine();
            
            System.out.print("Enter Player 2 name: ");
            String player2Name = scanner.nextLine();
            
            Player player1 = new HumanPlayer(player1Name, STARTING_CHIPS);
            Player player2 = new HumanPlayer(player2Name, STARTING_CHIPS);
            
            gameEngine.addPlayer(player1);
            gameEngine.addPlayer(player2);
            
            System.out.println("Game setup complete: " + player1Name + " vs " + player2Name);
            System.out.println("Each player starts with " + STARTING_CHIPS + " chips.");
            
            // Explain Texas Hold'em format
            explainPokerRules();
        }
    }
    
    /**
     * Explains the rules of Texas Hold'em to players.
     */
    private static void explainPokerRules() {
        System.out.println("\n--- Texas Hold'em Rules ---");
        System.out.println("In Texas Hold'em, each player receives 2 private cards.");
        System.out.println("Then 5 community cards are dealt in three stages:");
        System.out.println("- The Flop: First 3 community cards");
        System.out.println("- The Turn: 4th community card");
        System.out.println("- The River: 5th community card");
        System.out.println("Players use their 2 private cards AND the 5 community cards");
        System.out.println("to make the best possible 5-card poker hand.");
        System.out.println("----------------------------");
        
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Play through a round of poker.
     */
    private static void playRound() {
        // Pre-flop
        System.out.println("\n--- Pre-flop ---");
        System.out.println("Each player receives 2 private cards.");
        playBettingRound();
        
        // Flop
        gameEngine.dealCommunityCards(); // Deal the flop (3 cards)
        System.out.println("\n--- Flop ---");
        System.out.println("The first three community cards are dealt.");
        displayCommunityCards();
        playBettingRound();
        
        // Turn
        gameEngine.dealCommunityCards(); // Deal the turn (1 card)
        System.out.println("\n--- Turn ---");
        System.out.println("The fourth community card is dealt.");
        displayCommunityCards();
        playBettingRound();
        
        // River
        gameEngine.dealCommunityCards(); // Deal the river (1 card)
        System.out.println("\n--- River ---");
        System.out.println("The fifth and final community card is dealt.");
        displayCommunityCards();
        playBettingRound();
        
        // Move to showdown
        gameEngine.dealCommunityCards(); // This just changes state to SHOWDOWN
        
        // Showdown
        System.out.println("\n--- Showdown ---");
        System.out.println("All players reveal their cards.");
        displayCommunityCards();
        showPlayerCards();
        evaluateWinners();
        
        // Ask to play another round
        playAnotherRound();
    }
    
    /**
     * Evaluates winners and distributes the pot.
     */
    private static void evaluateWinners() {
        // GameEngine.evaluateWinner now handles the evaluation and winner determination
        Player winner = gameEngine.evaluateWinner();
        
        if (winner == null) {
            System.out.println("\nIt's a tie! The pot is split.");
            // In a real implementation, split the pot evenly
        }
        // Winner announcement is now handled in the GameEngine.evaluateWinner method
        
        // Show updated chip counts
        System.out.println("\nCurrent chip counts:");
        for (Player player : gameEngine.getPlayers()) {
            System.out.println(player.getName() + ": " + player.getChipCount() + " chips");
        }
    }
    
    /**
     * Asks players if they want to play another round.
     */
    private static void playAnotherRound() {
        System.out.print("\nPlay another round? (y/n): ");
        String answer = scanner.next().trim().toLowerCase();
        
        if (answer.startsWith("y")) {
            if (gameEngine.startNewRound()) {
                System.out.println("\nStarting a new round...");
                playRound();
            } else {
                System.out.println("Failed to start a new round. Some players may be out of chips.");
            }
        } else {
            System.out.println("\nThanks for playing Poker Clone!");
            
            // Show final results
            System.out.println("\nFinal Results:");
            for (Player player : gameEngine.getPlayers()) {
                System.out.println(player.getName() + " finished with " + player.getChipCount() + " chips");
            }
        }
    }
    
    /**
     * Handles a betting round among players.
     */
    private static void playBettingRound() {
        System.out.println("\n--- Betting Round ---");
        
        int currentBet = 0;
        
        for (Player player : gameEngine.getPlayers()) {
            List<Card> hand = gameEngine.getPlayerHand(player);
            List<Card> community = gameEngine.getCommunityCards();
            
            // Get player action
            PlayerAction action = player.getAction(hand, community, currentBet, gameEngine.getPotAmount());
            
            // Process the action
            switch (action) {
                case FOLD:
                    System.out.println(player.getName() + " folds.");
                    // In a complete implementation, folded players would be tracked
                    break;
                    
                case CHECK:
                    System.out.println(player.getName() + " checks.");
                    break;
                    
                case CALL:
                    if (currentBet > 0) {
                        System.out.println(player.getName() + " calls " + currentBet + " chips.");
                        player.removeChips(currentBet);
                        gameEngine.addToPot(currentBet);
                    }
                    break;
                    
                case RAISE:
                    int raiseAmount = currentBet + 20; // Simple fixed raise for demo
                    System.out.println(player.getName() + " raises to " + raiseAmount + " chips.");
                    player.removeChips(raiseAmount);
                    gameEngine.addToPot(raiseAmount);
                    currentBet = raiseAmount;
                    break;
                    
                case ALL_IN:
                    int allInAmount = player.getChipCount();
                    System.out.println(player.getName() + " goes ALL IN with " + allInAmount + " chips!");
                    player.removeChips(allInAmount);
                    gameEngine.addToPot(allInAmount);
                    if (allInAmount > currentBet) {
                        currentBet = allInAmount;
                    }
                    break;
            }
            
            // Pause briefly to let players see the action
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Ignore
            }
        }
        
        System.out.println("Betting round complete.");
        System.out.println("--------------------");
    }
    
    /**
     * Displays the current community cards.
     */
    private static void displayCommunityCards() {
        System.out.println("\nCommunity Cards:");
        for (Card card : gameEngine.getCommunityCards()) {
            System.out.println("  " + card.toString());
        }
    }
    
    /**
     * Shows all players' cards at showdown.
     */
    private static void showPlayerCards() {
        System.out.println("\nPlayer Cards:");
        for (Player player : gameEngine.getPlayers()) {
            System.out.println(player.getName() + ":");
            for (Card card : gameEngine.getPlayerHand(player)) {
                System.out.println("  " + card.toString());
            }
        }
    }
}
