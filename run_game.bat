@echo off
REM filepath: /workspaces/pokerclone/run_game.bat
REM Windows launcher for the Poker Clone game

echo Starting Poker Clone...
java -jar pokerclone.jar

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Error running the game. Please make sure Java is installed on your computer.
    echo You can download Java from: https://www.java.com/download/
    pause
    exit /b 1
)
