package org.graphics;

import javax.swing.*;
import java.awt.*;

public class HiveMain {
    static JPanel mainPanel;

    static MainMenu mainMenu;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Hive");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        // CardLayout to switch between panels
        CardLayout cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainMenu = new MainMenu();
        //JPanel gamePanel = new JPanel(); // Game board
        // ... more panels as needed

        mainPanel.add(mainMenu, "Menu");
        //mainPanel.add(gamePanel, "Game");
        // ... add more panels

        // Switching panels
        // cardLayout.show(mainPanel, "Game"); to show the game panel

        frame.add(mainPanel);
        frame.setVisible(true);
    }
}
