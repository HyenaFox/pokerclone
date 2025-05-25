#!/bin/bash
# Script to compile and run the poker game

echo "Compiling Java files..."
cd /workspaces/pokerclone/.devcontainer/demo
javac -d target src/main/java/com/example/*.java src/main/java/com/example/gui/*.java

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo "Running poker game..."
    cd target
    java com.example.PokerLauncher
else
    echo "Compilation failed!"
fi
