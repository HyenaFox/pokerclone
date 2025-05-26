@echo off
REM Launcher script for Poker Clone game

REM Check for Java installation
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo Java not found. Please install Java to run this game.
    exit /b 1
)

REM Run the game
java -jar pokerclone.jar

REM Exit with the same code as the Java process
exit /b %ERRORLEVEL%
