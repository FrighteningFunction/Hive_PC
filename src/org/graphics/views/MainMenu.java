package org.graphics.views;

import org.game.GameLogic;
import org.game.GraphicLogger;
import org.graphics.HiveMain;
import org.graphics.controllers.BoardController;
import org.graphics.controllers.GamePanelController;
import org.graphics.controllers.PlayerPanelController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.System.exit;

public class MainMenu extends JPanel {
    private transient JButton newGame = new JButton("New game");

    private transient JButton exit = new JButton ("exit");

    private transient MenuButtonsPressed menuListener = new MenuButtonsPressed();

    JPanel mainPanel = new JPanel();

    public MainMenu(){
        this.setLayout(new GridBagLayout());
        this.add(mainPanel);
        newGame.setActionCommand("newGame");
        newGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit.setActionCommand("exit");
        exit.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(newGame);
        mainPanel.add(exit);

        newGame.addActionListener(menuListener);
        exit.addActionListener(menuListener);

        GraphicLogger.getLogger().info("MainMenu created successfully.");
    }

    class MenuButtonsPressed implements ActionListener {
        public void actionPerformed(ActionEvent e){
            String c = e.getActionCommand();
            switch (c){
                case "newGame":
                    startNewGame();
                    break;
                case "exit":
                    exit(0);
                    break;
                default:
                    GraphicLogger.getLogger().fatal("Unknown MainMenu Command issued!");
                    exit(1);
                    break;
            }
        }
    }

    //todo: nem kellene másképp összekötni őket? Vagy a kontrollereket tényleg hozzuk létre itt így?
    public void startNewGame(){
        GameLogic gameLogic = GameLogic.getInstance();
        GamePanel gamePanel = HiveMain.getGamePanel();
        new PlayerPanelController(gamePanel.getBlackPlayerPanelView(), gameLogic.getBlackPlayer());
        new PlayerPanelController(gamePanel.getWhitePlayerPanelView(), gameLogic.getWhitePlayer());
        new BoardController(gamePanel.getBoardView(), gameLogic.getBoard());
        new GamePanelController(gameLogic, gamePanel);

        gameLogic.newGame();

        HiveMain.getCardLayout().show(HiveMain.getMainPanel(), "Game");
    }
}
