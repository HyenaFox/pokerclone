# Poker Clone

A Java-based poker game implementation featuring both console and graphical user interfaces.

## Features

- Texas Hold'em style poker game
- Play against AI opponent
- Full poker hand evaluation with all hand types (Royal Flush, Straight Flush, Four of a Kind, etc.)
- Two modes of play:
  - Console-based text interface
  - Graphical user interface with card visualization

## How to Play on Your Computer

### Building the Executable JAR

To create an executable JAR file that you can run on any computer with Java:

1. Build the executable JAR:
   ```
   ./build.sh
   ```
   This creates a file called `pokerclone.jar` in the main directory.

2. Copy this JAR file along with the launcher scripts (`run_game.bat` for Windows, `run_game.sh` for Mac/Linux) to your computer.

### Running the Game

#### On Windows:
- Double-click the `run_game.bat` file
- Alternatively, open a command prompt and run:
  ```
  java -jar pokerclone.jar
  ```

#### On Mac/Linux:
- Make the script executable: `chmod +x run_game.sh`
- Run the script: `./run_game.sh`
- Alternatively: `java -jar pokerclone.jar`

### Requirements
- Java Runtime Environment (JRE) 8 or higher
- Download Java from: https://www.java.com/download/

## Development

To run the game in development mode:

1. Execute the run script:
   ```
   ./run.sh
   ```
2. Choose between console mode or GUI mode when prompted

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
