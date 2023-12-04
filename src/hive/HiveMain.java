package hive;

import hive.graphics.views.GamePanel;
import hive.graphics.views.MainMenu;
import hive.graphics.views.SideMenu;

import javax.swing.*;
import java.awt.*;

/**
 * Ez az osztály felelős a fő panelek létrehozásáért, valamint az applikáció futtatásáért.
 */
public class HiveMain {
    static CardLayout mainCardLayout = new CardLayout();

    static JPanel mainPanel = new JPanel(mainCardLayout);

    static MainMenu mainMenu = new MainMenu();

    static GamePanel gamePanel = new GamePanel();

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

        mainPanel.add(mainMenu, "Menu");
        mainPanel.add(gamePanel, "Game");

        mainCardLayout.show(mainPanel, "Menu");
        SideMenu sideBar = new SideMenu();
        frame.setJMenuBar(sideBar.getMenuBar());

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    public static GamePanel getGamePanel() {
        return gamePanel;
    }
}
