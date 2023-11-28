package org.graphics;

import org.graphics.views.GamePanel;
import org.graphics.views.MainMenu;

import javax.swing.*;
import java.awt.*;

public class HiveMain {
    static JPanel mainPanel;

    static MainMenu mainMenu = new MainMenu();

    static GamePanel gamePanel = new GamePanel();

    public static JPanel getMainPanel(){
        return mainPanel;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Hive");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setResizable(false);

        // CardLayout to switch between panels
        CardLayout cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(mainMenu, "Menu");
        mainPanel.add(gamePanel, "Game");
        // ... add more panels

        // Switching panels
        // cardLayout.show(mainPanel, "Game"); to show the game panel

        frame.add(mainPanel);
        frame.setVisible(true);
    }
}
