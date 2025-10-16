package com.dictionary.main;

import com.dictionary.pl.DictionaryGUI;

public class Main {
    public static void main(String[] args) {
        // Launch the GUI application
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel for native appearance
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Failed to set Look and Feel: " + e.getMessage());
            }
            // Create and show the GUI
            new DictionaryGUI().setVisible(true);
        });
    }
}