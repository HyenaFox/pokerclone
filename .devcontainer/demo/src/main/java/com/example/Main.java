package com.example;

import com.example.Player.PlayerAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        
        // Initialize variables for tracking betting
        int lastRaisePlayerIndex = -1;
        List<Player> activePlayers = new ArrayList<>(gameEngine.getPlayers());
        List<Player> foldedPlayers = new ArrayList<>();
        Map<Player, Integer> playerBets = new HashMap<>();
        
        // Track highest bet of the round
        int highestBet = 0;
        
        // Continue betting until all active players have either called or folded
        int currentIndex = 0;
        Player currentPlayer;
        boolean bettingComplete = false;
        
        while (!bettingComplete) {
            if (activePlayers.size() <= 1) {
                // Only one player left - they win by default
                System.out.println("Only one active player remains.");
                break;
            }
            
            // Get the current player
            currentPlayer = activePlayers.get(currentIndex);
            int playerBet = playerBets.getOrDefault(currentPlayer, 0);
            int toCall = highestBet - playerBet;
            
            // Display betting information
            System.out.println("\nCurrent pot: " + gameEngine.getPotAmount());
            System.out.println("Current bet: " + highestBet);
            System.out.println(currentPlayer.getName() + "'s turn");
            
            if (toCall > 0) {
                System.out.println("Amount to call: " + toCall);
            }
            
            System.out.println(currentPlayer.getName() + " has " + currentPlayer.getChipCount() + " chips");
            
            // Get the player's hand
            List<Card> hand = gameEngine.getPlayerHand(currentPlayer);
            List<Card> community = gameEngine.getCommunityCards();
            
            // Get player action
            PlayerAction action = currentPlayer.getAction(hand, community, toCall, gameEngine.getPotAmount());
            
            // Process the action
            switch (action) {
                case FOLD:
                    System.out.println(currentPlayer.getName() + " folds.");
                    foldedPlayers.add(currentPlayer);
                    activePlayers.remove(currentPlayer);
                    currentIndex--; // Adjust for the removed player
                    break;
                    
                case CHECK:
                    if (toCall > 0) {
                        System.out.println("Cannot check when there's a bet. Treating as FOLD.");
                        foldedPlayers.add(currentPlayer);
                        activePlayers.remove(currentPlayer);
                        currentIndex--;
                    } else {
                        System.out.println(currentPlayer.getName() + " checks.");
                    }
                    break;
                    
                case CALL:
                    if (toCall > 0) {
                        // Ensure player has enough chips
                        int callAmount = Math.min(toCall, currentPlayer.getChipCount());
                        
                        if (callAmount == currentPlayer.getChipCount()) {
                            System.out.println(currentPlayer.getName() + " calls and is ALL IN with " + callAmount + " chips!");
                        } else {
                            System.out.println(currentPlayer.getName() + " calls " + callAmount + " chips.");
                        }
                        
                        currentPlayer.removeChips(callAmount);
                        gameEngine.addToPot(callAmount);
                        playerBets.put(currentPlayer, playerBet + callAmount);
                    } else {
                        // Treated as a check
                        System.out.println(currentPlayer.getName() + " checks.");
                    }
                    break;
                    
                case RAISE:
                    // Ask for raise amount
                    int minRaise = toCall + 20; // Minimum raise
                    int raiseAmount = minRaise;
                    
                    // If this is a human player, get custom raise amount
                    if (currentPlayer instanceof HumanPlayer) {
                        System.out.println("Minimum raise: " + minRaise);
                        System.out.print("Enter raise amount: ");
                        
                        try {
                            raiseAmount = scanner.nextInt();
                            while (raiseAmount < minRaise || raiseAmount > currentPlayer.getChipCount()) {
                                if (raiseAmount < minRaise) {
                                    System.out.println("Raise must be at least " + minRaise);
                                } else {
                                    System.out.println("You don't have enough chips. Maximum: " + currentPlayer.getChipCount());
                                }
                                System.out.print("Enter raise amount: ");
                                raiseAmount = scanner.nextInt();
                            }
                        } catch (Exception e) {
                            // Default to minimum raise if invalid input
                            raiseAmount = minRaise;
                        }
                    } else {
                        // AI player - randomize raise amount between min and twice min
                        int maxRaise = Math.min(currentPlayer.getChipCount(), minRaise * 2);
                        raiseAmount = minRaise + (int)(Math.random() * (maxRaise - minRaise + 1));
                    }
                    
                    // Process the raise
                    System.out.println(currentPlayer.getName() + " raises to " + raiseAmount + " chips.");
                    currentPlayer.removeChips(raiseAmount);
                    gameEngine.addToPot(raiseAmount);
                    playerBets.put(currentPlayer, playerBet + raiseAmount);
                    highestBet = playerBet + raiseAmount;
                    lastRaisePlayerIndex = activePlayers.indexOf(currentPlayer);
                    break;
                    
                case ALL_IN:
                    int allInAmount = currentPlayer.getChipCount();
                    System.out.println(currentPlayer.getName() + " goes ALL IN with " + allInAmount + " chips!");
                    currentPlayer.removeChips(allInAmount);
                    gameEngine.addToPot(allInAmount);
                    
                    int newBet = playerBet + allInAmount;
                    playerBets.put(currentPlayer, newBet);
                    
                    if (newBet > highestBet) {
                        highestBet = newBet;
                        lastRaisePlayerIndex = activePlayers.indexOf(currentPlayer);
                    }
                    break;
            }
            
            // Move to the next player
            currentIndex = (currentIndex + 1) % activePlayers.size();
            
            // Check if betting is complete
            if (lastRaisePlayerIndex == -1) {
                // No one has raised, betting is complete when we've gone around the table once
                if (currentIndex == 0) {
                    bettingComplete = true;
                }
            } else {
                // Someone has raised, betting is complete when we come back to that player
                if (currentIndex == (lastRaisePlayerIndex + 1) % activePlayers.size()) {
                    bettingComplete = true;
                }
            }
            
            // Pause briefly to let players see the action
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // Ignore
            }
        }
        
        // Display betting round summary
        System.out.println("\nBetting round complete.");
        System.out.println("Current pot: " + gameEngine.getPotAmount() + " chips");
        
        // Show player chip counts
        for (Player p : gameEngine.getPlayers()) {
            System.out.println(p.getName() + " has " + p.getChipCount() + " chips");
        }
        
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
