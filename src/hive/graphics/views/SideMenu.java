package hive.graphics.views;

import hive.Commands;
import hive.GraphicLogger;
import hive.HiveMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SideMenu {
    CardLayout mainCardLayout = HiveMain.getCardLayout();

    JMenuBar sideBar;

    public SideMenu(){
        sideBar = new JMenuBar();

        JMenu menu = new JMenu("Options");
        sideBar.add(menu);

        JMenuItem newGame = new JMenuItem("New Game");
        JMenuItem saveGame = new JMenuItem("Save Game");
        JMenuItem loadGame = new JMenuItem("Load Game");

        menu.add(newGame);
        newGame.setActionCommand("newGame");
        newGame.addActionListener(new sideMenuListener());
        menu.add(saveGame);
        saveGame.setActionCommand("saveGame");
        saveGame.addActionListener(new sideMenuListener());
        menu.add(loadGame);
        loadGame.setActionCommand("loadGame");
        loadGame.addActionListener(new sideMenuListener());
    }

    public JMenuBar getMenuBar(){
        return sideBar;
    }

    public class sideMenuListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            switch (command){
                case "newGame":
                    Commands.startNewGame();
                    break;
                case "saveGame":
                    Commands.saveGame();
                    break;
                case "loadGame":
                    Commands.loadGame();
                    break;
                default:
                    GraphicLogger.getLogger().error("Invalid command got at SideMenu!");
                    break;
            }
        }
    }


}
