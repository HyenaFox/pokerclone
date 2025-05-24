package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a standard deck of 52 playing cards
 */
public class Deck 
{
    private List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
        createDeck();
    }

    private void createDeck() {
        // For each suit
        for (Suit suit : Suit.values()) {
            // For each rank (1-13, where Ace=1, Jack=11, Queen=12, King=13)
            for (int rank = 1; rank <= 13; rank++) {
                cards.add(new Card(rank, suit));
            }
        }
    }

    public List<Card> getCards() {
        return this.cards;
    }

    public static void main(String[] args)
    {
        Deck deck = new Deck();
        System.out.println("Created a deck with " + deck.getCards().size() + " cards");
        
        // Print out all the cards to verify
        for (Card card : deck.getCards()) {
            System.out.println(card.getRank() + " of " + card.getSuit());
        }
    }
}
