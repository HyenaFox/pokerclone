#!/bin/bash
# Build script for Poker Clone game

echo "Building Poker Clone game..."

# Check for Maven installation
if command -v mvn &> /dev/null; then
    # Build using Maven
    cd .devcontainer/demo
    mvn clean package
    if [ $? -eq 0 ]; then
        cp target/demo-1.0-SNAPSHOT-jar-with-dependencies.jar ../../pokerclone.jar
        echo "Build successful! The game is ready to play."
        echo "Run './run_game.sh' (Linux/Mac) or 'run_game.bat' (Windows) to start the game."
    else
        echo "Build failed. Please check for errors."
        exit 1
    fi
else
    echo "Maven not found. Please install Maven to build this project."
    exit 1
fi
