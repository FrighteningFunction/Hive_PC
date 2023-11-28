package org.graphics.views;

import org.game.GameLogic;
import org.game.GraphicLogger;
import org.game.Player;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private PlayerPanelView whitePlayerPanelView;
    private PlayerPanelView blackPlayerPanelView;

    private BoardView boardView;

    public GamePanel() {
        this.setLayout(new GridLayout(3, 1));
        boardView = BoardView.getInstance();
        whitePlayerPanelView = new PlayerPanelView();
        blackPlayerPanelView = new PlayerPanelView();

        this.add(blackPlayerPanelView);
        this.add(boardView);
        this.add(whitePlayerPanelView);

        GraphicLogger.getLogger().info("GamePanel was created successfully.");
    }

    public PlayerPanelView getWhitePlayerPanelView() {
        return whitePlayerPanelView;
    }

    public PlayerPanelView getBlackPlayerPanelView() {
        return blackPlayerPanelView;
    }

    public BoardView getBoardView() {
        return boardView;
    }

    public void spawnVictoryDialog(Player p) {
        JOptionPane.showMessageDialog(this, String.format("%s player won!", p.getColor()));
    }
}
