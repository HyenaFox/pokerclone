package com.example;

public class Card {
    private Suit suit;
    private int rank;

    public Card(int rank, Suit suit) {
        this.suit = suit;
        this.rank = rank;
    }
    
    public Suit getSuit() {
        return this.suit;
    }

    public int getRank() {
        return this.rank;
    }
}
