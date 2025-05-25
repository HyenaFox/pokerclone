#!/bin/bash
# filepath: /workspaces/pokerclone/build.sh
# Script to build an executable JAR of the poker game

echo "Building executable JAR for the poker game..."

# Navigate to the project directory
cd "$(dirname "$0")/.devcontainer/demo"

# Use Maven to build the project
if command -v mvn &> /dev/null; then
    echo "Using Maven to build..."
    mvn clean package
    
    if [ $? -eq 0 ]; then
        # Copy the JAR to the root directory
        cp target/demo-1.0-SNAPSHOT.jar ../../pokerclone.jar
        echo "Executable JAR created successfully! The file is at: pokerclone.jar"
    else
        echo "Maven build failed."
        exit 1
    fi
else
    echo "Maven not found. Attempting manual build..."
    
    # Ensure target directory exists
    mkdir -p target/classes
    
    # Compile all Java files
    echo "Compiling Java files..."
    javac -d target/classes src/main/java/com/example/*.java src/main/java/com/example/gui/*.java
    
    if [ $? -eq 0 ]; then
        echo "Creating JAR file..."
        # Create a manifest file
        echo "Main-Class: com.example.PokerLauncher" > target/MANIFEST.MF
        
        # Create the JAR
        cd target/classes
        jar cfm ../pokerclone.jar ../MANIFEST.MF com/example/*.class com/example/gui/*.class
        
        # Copy JAR to root directory
        cp ../pokerclone.jar ../../../pokerclone.jar
        
        echo "Executable JAR created successfully! The file is at: pokerclone.jar"
    else
        echo "Compilation failed."
        exit 1
    fi
fi

echo "You can now run the game on any computer with Java installed using: java -jar pokerclone.jar"
