package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages deck operations like shuffling, dealing cards, and drawing hands for poker games.
 */
public class DeckManager {
    private Deck deck;
    private List<Card> discardPile;
    
    /**
     * Creates a new DeckManager with a fresh deck of cards.
     */
    public DeckManager() {
        this.deck = new Deck();
        this.discardPile = new ArrayList<>();
    }
    
    /**
     * Shuffles the deck of cards.
     */
    public void shuffle() {
        Collections.shuffle(deck.getCards());
    }
    
    /**
     * Draws a single card from the top of the deck.
     * 
     * @return The top card from the deck, or null if the deck is empty
     */
    public Card drawCard() {
        List<Card> cards = deck.getCards();
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(cards.size() - 1);
    }
    
    /**
     * Draws a poker hand with the specified number of cards.
     * 
     * @param handSize The number of cards to include in the hand
     * @return A list of cards representing the drawn hand
     */
    public List<Card> drawHand(int handSize) {
        List<Card> hand = new ArrayList<>();
        for (int i = 0; i < handSize; i++) {
            Card card = drawCard();
            if (card == null) {
                break;
            }
            hand.add(card);
        }
        return hand;
    }
    
    /**
     * Draws a standard 5-card poker hand.
     * 
     * @return A list of 5 cards representing a poker hand
     */
    public List<Card> drawPokerHand() {
        return drawHand(5);
    }
    
    /**
     * Discards a card, adding it to the discard pile.
     * 
     * @param card The card to discard
     */
    public void discardCard(Card card) {
        if (card != null) {
            discardPile.add(card);
        }
    }
    
    /**
     * Returns all cards from discard pile to the deck and shuffles.
     */
    public void resetAndShuffle() {
        deck.getCards().addAll(discardPile);
        discardPile.clear();
        shuffle();
    }
    
    /**
     * Gets the number of cards remaining in the deck.
     * 
     * @return The number of cards in the deck
     */
    public int getRemainingCardCount() {
        return deck.getCards().size();
    }
    
    /**
     * Gets the current discard pile.
     * 
     * @return List of discarded cards
     */
    public List<Card> getDiscardPile() {
        return discardPile;
    }
}
