#!/bin/bash
# Launcher script for Poker Clone game

# Check for Java installation
if ! command -v java &> /dev/null; then
    echo "Java not found. Please install Java to run this game."
    exit 1
fi

# Run the game
java -jar pokerclone.jar

# Exit with the same code as the Java process
exit $?
