package com.example;

import java.util.*;

/**
 * GameEngine class that manages the poker game flow, rules, and player interactions.
 */
public class GameEngine {
    private DeckManager deckManager;
    private List<Player> players;
    private int currentPlayerIndex = 0;
    private int dealerIndex = -1;
    private int smallBlindIndex = 0;
    private int bigBlindIndex = 0;
    private int potAmount;
    private int smallBlindAmount;
    private int bigBlindAmount;
    private Map<Player, List<Card>> playerHands;
    private List<Card> communityCards;
    private GameState gameState;
    
    /**
     * Enum representing the various states of the poker game.
     */
    public enum GameState {
        WAITING_FOR_PLAYERS,
        PRE_FLOP,
        FLOP,
        TURN,
        RIVER,
        SHOWDOWN,
        GAME_OVER
    }
    
    /**
     * Creates a new GameEngine with initialized components.
     */
    public GameEngine() {
        this.deckManager = new DeckManager();
        this.players = new ArrayList<>();
        this.playerHands = new HashMap<>();
        this.communityCards = new ArrayList<>();
        this.gameState = GameState.WAITING_FOR_PLAYERS;
        this.smallBlindAmount = 5; // Default small blind
        this.bigBlindAmount = 10;  // Default big blind
        this.potAmount = 0;
    }
    
    /**
     * Creates a new GameEngine with custom blind amounts.
     * 
     * @param smallBlind The small blind amount
     * @param bigBlind The big blind amount
     */
    public GameEngine(int smallBlind, int bigBlind) {
        this();
        this.smallBlindAmount = smallBlind;
        this.bigBlindAmount = bigBlind;
    }
    
    /**
     * Adds a player to the game.
     * 
     * @param player The player to add
     * @return true if the player was added successfully
     */
    public boolean addPlayer(Player player) {
        if (gameState != GameState.WAITING_FOR_PLAYERS) {
            return false;
        }
        
        if (players.contains(player)) {
            return false;
        }
        
        players.add(player);
        return true;
    }
    
    /**
     * Removes a player from the game.
     * 
     * @param player The player to remove
     * @return true if the player was removed successfully
     */
    public boolean removePlayer(Player player) {
        if (gameState != GameState.WAITING_FOR_PLAYERS && gameState != GameState.GAME_OVER) {
            return false;
        }
        
        return players.remove(player);
    }
    
    /**
     * Starts a new round of poker.
     * 
     * @return true if the game started successfully
     */
    public boolean startNewRound() {
        if (players.size() < 2 || gameState != GameState.WAITING_FOR_PLAYERS) {
            return false;
        }
        
        // Reset game state
        deckManager.resetAndShuffle();
        playerHands.clear();
        communityCards.clear();
        potAmount = 0;
        
        // Rotate dealer and blinds positions
        if (dealerIndex == -1) {
            dealerIndex = 0;  // First game
        } else {
            dealerIndex = (dealerIndex + 1) % players.size();
        }
        
        // Set blinds
        smallBlindIndex = (dealerIndex + 1) % players.size();
        bigBlindIndex = (dealerIndex + 2) % players.size();
        
        // Deal cards to players
        dealPlayerCards();
        
        // Collect blinds
        collectBlinds();
        
        currentPlayerIndex = (bigBlindIndex + 1) % players.size();
        gameState = GameState.PRE_FLOP;
        
        return true;
    }
    
    /**
     * Deals two cards to each player.
     */
    private void dealPlayerCards() {
        // Clear any existing hands
        playerHands.clear();
        
        // Deal 2 cards to each player
        for (Player player : players) {
            List<Card> hand = deckManager.drawHand(2);
            playerHands.put(player, hand);
        }
    }
    
    /**
     * Collects the small and big blinds.
     */
    private void collectBlinds() {
        // This would interact with player chips/money in a real implementation
        // For now we just add to the pot
        potAmount += smallBlindAmount + bigBlindAmount;
    }
    
    /**
     * Draws community cards based on the current game state.
     */
    public void dealCommunityCards() {
        switch (gameState) {
            case PRE_FLOP:
                // Deal the flop (3 cards)
                for (int i = 0; i < 3; i++) {
                    communityCards.add(deckManager.drawCard());
                }
                gameState = GameState.FLOP;
                break;
                
            case FLOP:
                // Deal the turn (1 card)
                communityCards.add(deckManager.drawCard());
                gameState = GameState.TURN;
                break;
                
            case TURN:
                // Deal the river (1 card)
                communityCards.add(deckManager.drawCard());
                gameState = GameState.RIVER;
                break;
                
            case RIVER:
                gameState = GameState.SHOWDOWN;
                break;
                
            default:
                // Do nothing for other states
        }
    }
    
    /**
     * Enum representing standard poker hand types from highest to lowest.
     */
    public enum HandType {
        ROYAL_FLUSH("Royal Flush"),
        STRAIGHT_FLUSH("Straight Flush"),
        FOUR_OF_A_KIND("Four of a Kind"),
        FULL_HOUSE("Full House"),
        FLUSH("Flush"),
        STRAIGHT("Straight"),
        THREE_OF_A_KIND("Three of a Kind"),
        TWO_PAIR("Two Pair"),
        ONE_PAIR("One Pair"),
        HIGH_CARD("High Card");
        
        private final String displayName;
        
        HandType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Class to hold information about a player's evaluated hand.
     */
    public static class HandResult {
        private Player player;
        private int score;
        private HandType handType;
        
        public HandResult(Player player, int score, HandType handType) {
            this.player = player;
            this.score = score;
            this.handType = handType;
        }
        
        public Player getPlayer() {
            return player;
        }
        
        public int getScore() {
            return score;
        }
        
        public HandType getHandType() {
            return handType;
        }
    }
    
    /**
     * Evaluates player hands and determines the winner.
     * 
     * @return The winning player, or null in case of a tie
     */
    public Player evaluateWinner() {
        if (gameState != GameState.SHOWDOWN) {
            return null;
        }
        
        // Map to store each player's hand result
        Map<Player, HandResult> playerResults = new HashMap<>();
        Player winner = null;
        int highestScore = -1;
        
        for (Player player : players) {
            List<Card> hand = playerHands.get(player);
            if (hand == null) continue;
            
            // Calculate hand score according to standard poker hand rankings
            int score = calculateHandScore(hand, communityCards);
            
            // Determine hand type based on score
            HandType handType = determineHandType(score);
            
            // Store player's result
            playerResults.put(player, new HandResult(player, score, handType));
            
            if (score > highestScore) {
                highestScore = score;
                winner = player;
            }
        }
        
        gameState = GameState.GAME_OVER;
        
        // Add pot to winner's chips
        if (winner != null) {
            winner.addChips(potAmount);
            System.out.println("\nWinner: " + winner.getName() + " with " + playerResults.get(winner).getHandType().getDisplayName());
        }
        
        return winner;
    }
    
    /**
     * Determines the hand type based on the calculated score.
     * 
     * @param score The hand score
     * @return The hand type
     */
    public HandType determineHandType(int score) {
        if (score >= 9000000) {
            return HandType.ROYAL_FLUSH;
        } else if (score >= 8000000) {
            return HandType.STRAIGHT_FLUSH;
        } else if (score >= 7000000) {
            return HandType.FOUR_OF_A_KIND;
        } else if (score >= 6000000) {
            return HandType.FULL_HOUSE;
        } else if (score >= 5000000) {
            return HandType.FLUSH;
        } else if (score >= 4000000) {
            return HandType.STRAIGHT;
        } else if (score >= 3000000) {
            return HandType.THREE_OF_A_KIND;
        } else if (score >= 2000000) {
            return HandType.TWO_PAIR;
        } else if (score >= 1000000) {
            return HandType.ONE_PAIR;
        } else {
            return HandType.HIGH_CARD;
        }
    }
    
    /**
     * Properly evaluate a poker hand according to standard poker hand rankings.
     * 
     * Hand rankings from highest to lowest:
     * 1. Royal Flush (A, K, Q, J, 10 of same suit)
     * 2. Straight Flush (5 consecutive cards of same suit)
     * 3. Four of a Kind (4 cards of same rank)
     * 4. Full House (3 cards of one rank, 2 of another)
     * 5. Flush (5 cards of same suit)
     * 6. Straight (5 consecutive cards)
     * 7. Three of a Kind (3 cards of same rank)
     * 8. Two Pair (2 cards of one rank, 2 of another)
     * 9. One Pair (2 cards of same rank)
     * 10. High Card (highest card if no other hand)
     * 
     * @param playerCards The player's private cards
     * @param communityCards The community cards
     * @return A score representing the hand strength (higher is better)
     */
    private int calculateHandScore(List<Card> playerCards, List<Card> communityCards) {
        // Combine player's cards with community cards
        List<Card> allCards = new ArrayList<>(playerCards);
        allCards.addAll(communityCards);
        
        // Score is calculated as: HandType * 1000000 + Primary * 10000 + Secondary * 100 + Kickers
        // This ensures proper hand rankings (e.g. any straight flush beats any four of a kind)
        
        // Count cards by rank and suit
        Map<Integer, Integer> rankCounts = new HashMap<>();
        Map<Suit, List<Card>> suitGroups = new HashMap<>();
        
        for (Card card : allCards) {
            // Count ranks
            int rank = card.getRank();
            rankCounts.put(rank, rankCounts.getOrDefault(rank, 0) + 1);
            
            // Group by suit
            Suit suit = card.getSuit();
            if (!suitGroups.containsKey(suit)) {
                suitGroups.put(suit, new ArrayList<>());
            }
            suitGroups.get(suit).add(card);
        }
        
        // Check for Flush (including Straight Flush and Royal Flush)
        boolean hasFlush = false;
        List<Card> flushCards = null;
        for (List<Card> suitedCards : suitGroups.values()) {
            if (suitedCards.size() >= 5) {
                hasFlush = true;
                flushCards = suitedCards;
                break;
            }
        }
        
        // Check for Straight
        boolean hasStraight = false;
        int straightHighCard = 0;
        
        // Sort unique ranks for straight detection
        List<Integer> uniqueRanks = new ArrayList<>(rankCounts.keySet());
        Collections.sort(uniqueRanks);
        
        // Handle Ace as both 1 and 14 for straights
        if (rankCounts.containsKey(1)) {
            uniqueRanks.add(14); // Add Ace as 14 at the end
        }
        
        // Find the longest consecutive sequence
        int currentStreak = 1;
        int currentHighCard = uniqueRanks.get(0);
        
        for (int i = 1; i < uniqueRanks.size(); i++) {
            if (uniqueRanks.get(i) == uniqueRanks.get(i-1) + 1) {
                // Continuing a streak
                currentStreak++;
                currentHighCard = uniqueRanks.get(i);
                
                if (currentStreak >= 5) {
                    hasStraight = true;
                    straightHighCard = currentHighCard;
                }
            } else if (uniqueRanks.get(i) > uniqueRanks.get(i-1) + 1) {
                // Streak broken
                currentStreak = 1;
                currentHighCard = uniqueRanks.get(i);
            }
        }
        
        // Check for Straight Flush or Royal Flush
        boolean hasStraightFlush = false;
        int straightFlushHighCard = 0;
        
        if (hasFlush && flushCards != null) {
            // Sort flush cards by rank
            flushCards.sort((c1, c2) -> {
                // Convert Ace (1) to 14 for high card comparison
                int rank1 = c1.getRank() == 1 ? 14 : c1.getRank();
                int rank2 = c2.getRank() == 1 ? 14 : c2.getRank();
                return Integer.compare(rank1, rank2);
            });
            
            // Check for consecutive ranks in the flush cards
            for (int i = 0; i <= flushCards.size() - 5; i++) {
                boolean isConsecutive = true;
                
                for (int j = i; j < i + 4; j++) {
                    // Handle Ace as 1 or 14
                    int currentRank = flushCards.get(j).getRank() == 1 ? 14 : flushCards.get(j).getRank();
                    int nextRank = flushCards.get(j+1).getRank() == 1 ? 14 : flushCards.get(j+1).getRank();
                    
                    if (nextRank != currentRank + 1) {
                        isConsecutive = false;
                        break;
                    }
                }
                
                if (isConsecutive) {
                    hasStraightFlush = true;
                    int highCardRank = flushCards.get(i+4).getRank();
                    straightFlushHighCard = highCardRank == 1 ? 14 : highCardRank;
                    break;
                }
            }
        }
        
        // Find Four of a Kind, Full House, Three of a Kind, Two Pair, and Pair
        int fourOfAKindRank = 0;
        int threeOfAKindRank = 0;
        int highPairRank = 0;
        int lowPairRank = 0;
        
        // Sort rank counts by value (count) in descending order
        List<Map.Entry<Integer, Integer>> sortedCounts = new ArrayList<>(rankCounts.entrySet());
        sortedCounts.sort((e1, e2) -> {
            // First by count (descending)
            int countCompare = Integer.compare(e2.getValue(), e1.getValue());
            if (countCompare != 0) {
                return countCompare;
            }
            
            // Then by rank (descending, with Ace as high)
            int rank1 = e1.getKey() == 1 ? 14 : e1.getKey();
            int rank2 = e2.getKey() == 1 ? 14 : e2.getKey();
            return Integer.compare(rank2, rank1);
        });
        
        // Find the highest combinations
        for (Map.Entry<Integer, Integer> entry : sortedCounts) {
            int rank = entry.getKey();
            int count = entry.getValue();
            int adjustedRank = rank == 1 ? 14 : rank; // Ace is high
            
            if (count == 4 && fourOfAKindRank == 0) {
                fourOfAKindRank = adjustedRank;
            } else if (count == 3 && threeOfAKindRank == 0) {
                threeOfAKindRank = adjustedRank;
            } else if (count == 2) {
                if (highPairRank == 0) {
                    highPairRank = adjustedRank;
                } else if (lowPairRank == 0) {
                    lowPairRank = adjustedRank;
                }
            }
        }
        
        // Find high cards (kickers)
        List<Integer> kickers = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : sortedCounts) {
            int rank = entry.getKey();
            int adjustedRank = rank == 1 ? 14 : rank; // Ace is high
            
            // Skip ranks already used in combinations
            if (adjustedRank == fourOfAKindRank || adjustedRank == threeOfAKindRank || 
                adjustedRank == highPairRank || adjustedRank == lowPairRank) {
                continue;
            }
            
            kickers.add(adjustedRank);
        }
        
        Collections.sort(kickers, Collections.reverseOrder());
        
        // Calculate hand score based on the hand type and rank values
        
        // 1. Royal Flush (Straight Flush with Ace high)
        if (hasStraightFlush && straightFlushHighCard == 14) {
            return 9000000; // Best possible hand
        }
        
        // 2. Straight Flush
        if (hasStraightFlush) {
            return 8000000 + straightFlushHighCard * 10000;
        }
        
        // 3. Four of a Kind
        if (fourOfAKindRank > 0) {
            int kickerValue = kickers.isEmpty() ? 0 : kickers.get(0);
            return 7000000 + fourOfAKindRank * 10000 + kickerValue;
        }
        
        // 4. Full House
        if (threeOfAKindRank > 0 && highPairRank > 0) {
            return 6000000 + threeOfAKindRank * 10000 + highPairRank * 100;
        }
        
        // 5. Flush
        if (hasFlush) {
            int value = 5000000;
            // Add up to 5 highest cards as kickers
            for (int i = 0; i < Math.min(5, flushCards.size()); i++) {
                int cardRank = flushCards.get(flushCards.size() - 1 - i).getRank();
                int adjustedRank = cardRank == 1 ? 14 : cardRank; // Ace is high
                value += adjustedRank * Math.pow(10, 3 - i);
            }
            return value;
        }
        
        // 6. Straight
        if (hasStraight) {
            return 4000000 + straightHighCard * 10000;
        }
        
        // 7. Three of a Kind
        if (threeOfAKindRank > 0) {
            int kickerValue = 0;
            for (int i = 0; i < Math.min(2, kickers.size()); i++) {
                kickerValue += kickers.get(i) * Math.pow(10, 1 - i);
            }
            return 3000000 + threeOfAKindRank * 10000 + kickerValue;
        }
        
        // 8. Two Pair
        if (highPairRank > 0 && lowPairRank > 0) {
            int kickerValue = kickers.isEmpty() ? 0 : kickers.get(0);
            return 2000000 + highPairRank * 10000 + lowPairRank * 100 + kickerValue;
        }
        
        // 9. One Pair
        if (highPairRank > 0) {
            int kickerValue = 0;
            for (int i = 0; i < Math.min(3, kickers.size()); i++) {
                kickerValue += kickers.get(i) * Math.pow(10, 2 - i);
            }
            return 1000000 + highPairRank * 10000 + kickerValue;
        }
        
        // 10. High Card
        int value = 0;
        for (int i = 0; i < Math.min(5, kickers.size()); i++) {
            value += kickers.get(i) * Math.pow(10, 4 - i);
        }
        
        return value;
    }
    
    /**
     * Public method to allow AI players to access the hand score calculation.
     * 
     * @param playerCards The player's private cards
     * @param communityCards The community cards
     * @return The calculated hand score
     */
    public int calculateHandScoreForAI(List<Card> playerCards, List<Card> communityCards) {
        return calculateHandScore(playerCards, communityCards);
    }
    
    /**
     * Gets the current pot amount.
     * 
     * @return The current pot amount
     */
    public int getPotAmount() {
        return potAmount;
    }
    
    /**
     * Gets the current community cards.
     * 
     * @return List of community cards
     */
    public List<Card> getCommunityCards() {
        return Collections.unmodifiableList(communityCards);
    }
    
    /**
     * Gets a player's hand.
     * 
     * @param player The player
     * @return The player's hand or null if the player doesn't have a hand
     */
    public List<Card> getPlayerHand(Player player) {
        List<Card> hand = playerHands.get(player);
        return (hand != null) ? Collections.unmodifiableList(hand) : null;
    }
    
    /**
     * Gets the current game state.
     * 
     * @return The current game state
     */
    public GameState getGameState() {
        return gameState;
    }
    
    /**
     * Gets the list of players.
     * 
     * @return List of players in the game
     */
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }
    
    /**
     * Adds chips to the pot.
     * 
     * @param amount The amount to add to the pot
     */
    public void addToPot(int amount) {
        if (amount > 0) {
            potAmount += amount;
        }
    }
}
