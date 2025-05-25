#!/bin/bash
# filepath: /workspaces/pokerclone/run_game.sh
# Unix/Mac launcher for the Poker Clone game

echo "Starting Poker Clone..."

# Check if java is installed
if command -v java &> /dev/null; then
    java -jar pokerclone.jar
else
    echo "Error: Java is not installed or not in your PATH."
    echo "Please install Java to run this game."
    echo "You can download Java from: https://www.java.com/download/"
    exit 1
fi
