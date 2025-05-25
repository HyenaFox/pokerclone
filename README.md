# Poker Clone

A Java-based poker game implementation featuring both console and graphical user interfaces.

## Features

- Texas Hold'em style poker game
- Play against AI opponent
- Full poker hand evaluation with all hand types (Royal Flush, Straight Flush, Four of a Kind, etc.)
- Two modes of play:
  - Console-based text interface
  - Graphical user interface with card visualization

## How to Run

1. Make sure you have Java installed on your system
2. Execute the run script:
   ```
   ./run.sh
   ```
3. Choose between console mode or GUI mode when prompted

## Game Rules

In Texas Hold'em, each player receives 2 private cards. Then 5 community cards are dealt in three stages:
- The Flop: First 3 community cards
- The Turn: 4th community card
- The River: 5th and final community card

Players use their 2 private cards AND the 5 community cards to make the best possible 5-card poker hand.

## Technical Implementation

The game features proper object-oriented design with classes for:
- Game engine and flow control
- Cards, deck, and deck management
- Players (human and AI)
- Hand evaluation according to standard poker rules
- GUI implementation using Java Swing
