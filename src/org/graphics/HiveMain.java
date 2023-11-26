package org.graphics;

import javax.swing.*;
import java.awt.*;

public class HiveMain {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Hive Game");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // CardLayout to switch between panels
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        // Panels for different screens
        JPanel menuPanel = new JPanel(); // Add components like buttons
        JPanel gamePanel = new JPanel(); // Game board
        // ... more panels as needed

        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(gamePanel, "Game");
        // ... add more panels

        // Switching panels
        // cardLayout.show(mainPanel, "Game"); to show the game panel

        frame.add(mainPanel);
        frame.setVisible(true);
    }
}
