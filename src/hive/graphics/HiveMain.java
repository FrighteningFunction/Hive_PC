package hive.graphics;

import hive.graphics.views.GamePanel;
import hive.graphics.views.MainMenu;

import javax.swing.*;
import java.awt.*;

public class HiveMain {
    static JPanel mainPanel;

    static MainMenu mainMenu = new MainMenu();

    static GamePanel gamePanel = new GamePanel();

    static CardLayout mainCardLayout = new CardLayout();

    private HiveMain(){}

    public static JPanel getMainPanel(){
        return mainPanel;
    }

    public static CardLayout getCardLayout(){
        return mainCardLayout;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Hive");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);

        mainPanel = new JPanel(mainCardLayout);

        mainPanel.add(mainMenu, "Menu");
        mainPanel.add(gamePanel, "Game");


        // Switching panels
        // cardLayout.show(mainPanel, "Game"); to show the game panel

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    public static GamePanel getGamePanel() {
        return gamePanel;
    }
}
