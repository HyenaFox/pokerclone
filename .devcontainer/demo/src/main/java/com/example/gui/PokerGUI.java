package com.example.gui;

import com.example.*;
import com.example.Player.PlayerAction;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Main GUI class for the poker game.
 */
public class PokerGUI extends JFrame {
    private static final int CARD_WIDTH = 80;
    private static final int CARD_HEIGHT = 120;
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;
    
    private GameEngine gameEngine;
    private Player humanPlayer;
    private Player aiPlayer;
    
    // GUI Components
    private JPanel mainPanel;
    private JPanel gamePanel;
    private JPanel controlPanel;
    private JPanel communityCardsPanel;
    private JPanel playerCardsPanel;
    private JPanel aiCardsPanel;
    private JPanel actionPanel;
    private JLabel statusLabel;
    private JLabel potLabel;
    private JLabel playerChipsLabel;
    private JLabel aiChipsLabel;
    
    // Card images
    private Map<String, BufferedImage> cardImages = new HashMap<>();
    private BufferedImage cardBackImage;
    
    // Current game state tracking
    private int currentBet = 0;
    
    /**
     * Creates the poker game GUI.
     */
    public PokerGUI() {
        super("Poker Clone");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initializeGame();
        initializeUI();
        loadCardImages();
        
        setVisible(true);
    }
    
    /**
     * Initialize the game engine and players.
     */
    private void initializeGame() {
        gameEngine = new GameEngine();
        humanPlayer = new GUIPlayer("Player", 1000);
        aiPlayer = new AIPlayer("Computer", 1000);
        
        gameEngine.addPlayer(humanPlayer);
        gameEngine.addPlayer(aiPlayer);
    }
    
    /**
     * Initialize the user interface components.
     */
    private void initializeUI() {
        // Main layout
        mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);
        
        // Game panel (center)
        gamePanel = new JPanel(new BorderLayout());
        mainPanel.add(gamePanel, BorderLayout.CENTER);
        
        // Community cards panel
        communityCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        communityCardsPanel.setBackground(new Color(0, 100, 0));  // Dark green background
        communityCardsPanel.setBorder(BorderFactory.createTitledBorder("Community Cards"));
        gamePanel.add(communityCardsPanel, BorderLayout.CENTER);
        
        // AI cards panel (top)
        aiCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        aiCardsPanel.setBackground(new Color(0, 100, 0));
        aiCardsPanel.setBorder(BorderFactory.createTitledBorder("Computer"));
        gamePanel.add(aiCardsPanel, BorderLayout.NORTH);
        
        // Player cards panel (bottom)
        playerCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        playerCardsPanel.setBackground(new Color(0, 100, 0));
        playerCardsPanel.setBorder(BorderFactory.createTitledBorder("Your Hand"));
        gamePanel.add(playerCardsPanel, BorderLayout.SOUTH);
        
        // Control panel (east)
        controlPanel = new JPanel(new BorderLayout());
        controlPanel.setPreferredSize(new Dimension(200, WINDOW_HEIGHT));
        mainPanel.add(controlPanel, BorderLayout.EAST);
        
        // Status panel (top of control panel)
        JPanel statusPanel = new JPanel(new GridLayout(4, 1));
        statusLabel = new JLabel("Welcome to Poker Clone!");
        potLabel = new JLabel("Pot: $0");
        playerChipsLabel = new JLabel("Your chips: $" + humanPlayer.getChipCount());
        aiChipsLabel = new JLabel("Computer chips: $" + aiPlayer.getChipCount());
        
        statusPanel.add(statusLabel);
        statusPanel.add(potLabel);
        statusPanel.add(playerChipsLabel);
        statusPanel.add(aiChipsLabel);
        controlPanel.add(statusPanel, BorderLayout.NORTH);
        
        // Action panel (bottom of control panel)
        actionPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        controlPanel.add(actionPanel, BorderLayout.SOUTH);
        
        // Add buttons for new game
        addNewGameControls();
    }
    
    /**
     * Add controls for starting a new game.
     */
    private void addNewGameControls() {
        actionPanel.removeAll();
        
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> startNewGame());
        
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> System.exit(0));
        
        actionPanel.add(new JLabel("Ready to play?"));
        actionPanel.add(newGameButton);
        actionPanel.add(quitButton);
        
        actionPanel.revalidate();
        actionPanel.repaint();
    }
    
    /**
     * Add controls for player actions during the game.
     */
    private void addGameActionControls() {
        actionPanel.removeAll();
        
        JButton checkCallButton = new JButton(currentBet > 0 ? "Call $" + currentBet : "Check");
        JButton raiseButton = new JButton("Raise");
        JButton foldButton = new JButton("Fold");
        
        checkCallButton.addActionListener(e -> {
            if (currentBet > 0) {
                humanPlayer.removeChips(currentBet);
                gameEngine.addToPot(currentBet);
                updateLabels();
                processPlayerAction(PlayerAction.CALL);
            } else {
                processPlayerAction(PlayerAction.CHECK);
            }
        });
        
        raiseButton.addActionListener(e -> {
            int raiseAmount = currentBet + 20;  // Simple fixed raise for demo
            if (raiseAmount <= humanPlayer.getChipCount()) {
                humanPlayer.removeChips(raiseAmount);
                gameEngine.addToPot(raiseAmount);
                currentBet = raiseAmount;
                updateLabels();
                processPlayerAction(PlayerAction.RAISE);
            } else {
                JOptionPane.showMessageDialog(this, "Not enough chips to raise!");
            }
        });
        
        foldButton.addActionListener(e -> {
            processPlayerAction(PlayerAction.FOLD);
        });
        
        actionPanel.add(new JLabel("Your action:"));
        actionPanel.add(checkCallButton);
        actionPanel.add(raiseButton);
        actionPanel.add(foldButton);
        
        actionPanel.revalidate();
        actionPanel.repaint();
    }
    
    /**
     * Load card images.
     */
    private void loadCardImages() {
        try {
            // We'll create simple colored shapes for cards
            // In a real implementation, you would load actual card images
            createCardImages();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading card images: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Create simple card images for the demo.
     */
    private void createCardImages() {
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        
        for (String suit : suits) {
            Color suitColor = (suit.equals("Hearts") || suit.equals("Diamonds")) ? Color.RED : Color.BLACK;
            for (int i = 0; i < ranks.length; i++) {
                String cardId = (i+1) + "_of_" + suit;
                cardImages.put(cardId, createCardImage(ranks[i], suit, suitColor));
            }
        }
        
        // Create card back image
        cardBackImage = createCardBackImage();
    }
    
    /**
     * Create a simple card image.
     */
    private BufferedImage createCardImage(String rank, String suit, Color color) {
        BufferedImage cardImage = new BufferedImage(CARD_WIDTH, CARD_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = cardImage.createGraphics();
        
        // Draw card background
        g.setColor(Color.WHITE);
        g.fillRoundRect(0, 0, CARD_WIDTH - 1, CARD_HEIGHT - 1, 10, 10);
        g.setColor(Color.BLACK);
        g.drawRoundRect(0, 0, CARD_WIDTH - 1, CARD_HEIGHT - 1, 10, 10);
        
        // Draw rank and suit
        g.setColor(color);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString(rank, 5, 20);
        g.drawString(suit.substring(0, 1), 5, 40);
        
        // Draw center symbol
        g.setFont(new Font("Arial", Font.BOLD, 32));
        String symbol = getSuitSymbol(suit);
        FontMetrics fm = g.getFontMetrics();
        int symbolWidth = fm.stringWidth(symbol);
        int symbolHeight = fm.getHeight();
        g.drawString(symbol, (CARD_WIDTH - symbolWidth) / 2, (CARD_HEIGHT + symbolHeight / 2) / 2);
        
        g.dispose();
        return cardImage;
    }
    
    /**
     * Create a card back image.
     */
    private BufferedImage createCardBackImage() {
        BufferedImage cardImage = new BufferedImage(CARD_WIDTH, CARD_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = cardImage.createGraphics();
        
        // Draw card background
        g.setColor(Color.WHITE);
        g.fillRoundRect(0, 0, CARD_WIDTH - 1, CARD_HEIGHT - 1, 10, 10);
        g.setColor(Color.BLACK);
        g.drawRoundRect(0, 0, CARD_WIDTH - 1, CARD_HEIGHT - 1, 10, 10);
        
        // Draw pattern
        g.setColor(new Color(0, 0, 128));
        for (int i = 0; i < CARD_HEIGHT; i += 10) {
            g.drawLine(0, i, CARD_WIDTH, i);
        }
        for (int i = 0; i < CARD_WIDTH; i += 10) {
            g.drawLine(i, 0, i, CARD_HEIGHT);
        }
        
        g.dispose();
        return cardImage;
    }
    
    /**
     * Get Unicode symbol for a suit.
     */
    private String getSuitSymbol(String suit) {
        switch (suit) {
            case "Hearts": return "♥";
            case "Diamonds": return "♦";
            case "Clubs": return "♣";
            case "Spades": return "♠";
            default: return "";
        }
    }
    
    /**
     * Start a new game.
     */
    private void startNewGame() {
        // Reset the game
        gameEngine.startNewRound();
        currentBet = 0;
        
        // Update UI
        updateLabels();
        updateCards(true);
        statusLabel.setText("New round started!");
        
        // Add game controls
        addGameActionControls();
    }
    
    /**
     * Update labels with current game state.
     */
    private void updateLabels() {
        potLabel.setText("Pot: $" + gameEngine.getPotAmount());
        playerChipsLabel.setText("Your chips: $" + humanPlayer.getChipCount());
        aiChipsLabel.setText("Computer chips: $" + aiPlayer.getChipCount());
    }
    
    /**
     * Update card display.
     * 
     * @param hideAICards Whether to hide AI cards
     */
    private void updateCards(boolean hideAICards) {
        // Clear panels
        playerCardsPanel.removeAll();
        aiCardsPanel.removeAll();
        communityCardsPanel.removeAll();
        
        // Display player cards
        List<Card> playerCards = gameEngine.getPlayerHand(humanPlayer);
        if (playerCards != null) {
            for (Card card : playerCards) {
                JLabel cardLabel = new JLabel(new ImageIcon(getCardImage(card)));
                playerCardsPanel.add(cardLabel);
            }
        }
        
        // Display AI cards
        List<Card> aiCards = gameEngine.getPlayerHand(aiPlayer);
        if (aiCards != null) {
            for (Card card : aiCards) {
                JLabel cardLabel = new JLabel(new ImageIcon(hideAICards ? cardBackImage : getCardImage(card)));
                aiCardsPanel.add(cardLabel);
            }
        }
        
        // Display community cards
        List<Card> communityCards = gameEngine.getCommunityCards();
        if (communityCards != null) {
            for (Card card : communityCards) {
                JLabel cardLabel = new JLabel(new ImageIcon(getCardImage(card)));
                communityCardsPanel.add(cardLabel);
            }
        }
        
        // Refresh UI
        playerCardsPanel.revalidate();
        playerCardsPanel.repaint();
        aiCardsPanel.revalidate();
        aiCardsPanel.repaint();
        communityCardsPanel.revalidate();
        communityCardsPanel.repaint();
    }
    
    /**
     * Get image for a specific card.
     */
    private BufferedImage getCardImage(Card card) {
        int rank = card.getRank();
        String suit = card.getSuit().toString();
        String cardId = rank + "_of_" + suit;
        return cardImages.get(cardId);
    }
    
    /**
     * Process player action and advance the game.
     */
    private void processPlayerAction(PlayerAction action) {
        // Handle player fold
        if (action == PlayerAction.FOLD) {
            handlePlayerFold();
            return;
        }
        
        // Display player's action
        statusLabel.setText("You chose to " + action.toString());
        
        // Get AI's action
        List<Card> aiHand = gameEngine.getPlayerHand(aiPlayer);
        List<Card> communityCards = gameEngine.getCommunityCards();
        PlayerAction aiAction = aiPlayer.getAction(aiHand, communityCards, currentBet, gameEngine.getPotAmount());
        
        // Process AI's action using a separate thread to avoid UI freezing
        Thread aiActionThread = new Thread(() -> {
            try {
                // Delay to show player action
                Thread.sleep(1000);
                
                // Update UI from EDT
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Computer chose to " + aiAction.toString());
                    
                    // Process AI's action
                    switch (aiAction) {
                        case FOLD:
                            handleAIFold();
                            return;
                        case CHECK:
                            advanceGameStage();
                            break;
                        case CALL:
                            if (currentBet > 0) {
                                aiPlayer.removeChips(currentBet);
                                gameEngine.addToPot(currentBet);
                                updateLabels();
                            }
                            advanceGameStage();
                            break;
                        case RAISE:
                            int aiRaiseAmount = currentBet + 20;
                            aiPlayer.removeChips(aiRaiseAmount);
                            gameEngine.addToPot(aiRaiseAmount);
                            currentBet = aiRaiseAmount;
                            updateLabels();
                            statusLabel.setText("Computer raised to $" + currentBet);
                            // Give player chance to respond to raise
                            addGameActionControls();
                            return; // Don't advance game yet
                        case ALL_IN:
                            int allInAmount = aiPlayer.getChipCount();
                            aiPlayer.removeChips(allInAmount);
                            gameEngine.addToPot(allInAmount);
                            if (allInAmount > currentBet) {
                                currentBet = allInAmount;
                            }
                            updateLabels();
                            statusLabel.setText("Computer goes ALL IN with $" + allInAmount);
                            // Give player chance to respond to all-in
                            addGameActionControls();
                            return; // Don't advance game yet
                    }
                });
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        aiActionThread.start();
    }
    
    /**
     * Handle AI fold action.
     */
    private void handleAIFold() {
        statusLabel.setText("Computer folded. You win!");
        humanPlayer.addChips(gameEngine.getPotAmount());
        updateLabels();
        addNewGameControls();
    }
    
    /**
     * Handle player fold action.
     */
    private void handlePlayerFold() {
        statusLabel.setText("You folded. Computer wins!");
        aiPlayer.addChips(gameEngine.getPotAmount());
        updateLabels();
        addNewGameControls();
    }
    
    /**
     * Advance the game to the next stage.
     */
    private void advanceGameStage() {
        GameEngine.GameState currentState = gameEngine.getGameState();
        
        // Determine next action based on current state
        switch (currentState) {
            case PRE_FLOP:
                gameEngine.dealCommunityCards(); // Deal the flop
                statusLabel.setText("Dealing the flop");
                break;
                
            case FLOP:
                gameEngine.dealCommunityCards(); // Deal the turn
                statusLabel.setText("Dealing the turn");
                break;
                
            case TURN:
                gameEngine.dealCommunityCards(); // Deal the river
                statusLabel.setText("Dealing the river");
                break;
                
            case RIVER:
                gameEngine.dealCommunityCards(); // Move to showdown
                showdown();
                return;
                
            default:
                // Should not reach here
                return;
        }
        
        // Reset current bet for new betting round
        currentBet = 0;
        
        // Update the display
        updateCards(true);
        
        // Add action controls for the next round of betting
        addGameActionControls();
    }
    
    /**
     * Handle showdown.
     */
    private void showdown() {
        statusLabel.setText("Showdown!");
        
        // Show all cards
        updateCards(false);
        
        // Evaluate winner
        Player winner = gameEngine.evaluateWinner();
        
        // Display result
        if (winner == humanPlayer) {
            JOptionPane.showMessageDialog(this, "You win $" + gameEngine.getPotAmount() + "!");
        } else if (winner == aiPlayer) {
            JOptionPane.showMessageDialog(this, "Computer wins with $" + gameEngine.getPotAmount() + "!");
        } else {
            JOptionPane.showMessageDialog(this, "It's a tie! The pot is split.");
        }
        
        // Update labels with final chip counts
        updateLabels();
        
        // Reset to new game controls
        addNewGameControls();
    }
    
    /**
     * Main method to start the application.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new PokerGUI());
    }
}
