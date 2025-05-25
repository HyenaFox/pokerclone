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
    
    public String getRankName() {
        switch (rank) {
            case 1:
                return "Ace";
            case 11:
                return "Jack";
            case 12:
                return "Queen";
            case 13:
                return "King";
            default:
                return String.valueOf(rank);
        }
    }
    
    @Override
    public String toString() {
        return getRankName() + " of " + suit;
    }
}
