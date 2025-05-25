package com.example;

import com.example.gui.PokerGUI;
import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

/**
 * Main launcher for the poker game that allows choosing between console and GUI versions.
 */
public class PokerLauncher {

    public static void main(String[] args) {
        // Check if GUI is available
        boolean guiAvailable = true;
        try {
            Toolkit.getDefaultToolkit();
        } catch (Throwable t) {
            System.out.println("GUI is not available in this environment.");
            guiAvailable = false;
        }
        
        if (!guiAvailable) {
            System.out.println("Starting in console-only mode...");
            Main.main(args);
            return;
        }
        
        System.out.println("Welcome to Poker Clone!");
        System.out.println("1: Play in console mode");
        System.out.println("2: Play in GUI mode");
        
        Scanner scanner = new Scanner(System.in);
        int choice;
        
        try {
            do {
                System.out.print("Enter your choice (1-2): ");
                while (!scanner.hasNextInt()) {
                    System.out.print("Invalid input. Enter a number (1-2): ");
                    scanner.next();
                }
                choice = scanner.nextInt();
            } while (choice < 1 || choice > 2);
            
            if (choice == 1) {
                // Launch console version
                System.out.println("Starting console version...");
                Main.main(args);
            } else {
                // Launch GUI version
                System.out.println("Starting GUI version...");
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    SwingUtilities.invokeLater(() -> new PokerGUI());
                } catch (Exception e) {
                    System.out.println("Error starting GUI mode: " + e.getMessage());
                    System.out.println("Falling back to console mode...");
                    Main.main(args);
                }
            }
        } finally {
            scanner.close();
        }
    }
}
