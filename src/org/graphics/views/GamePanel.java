package org.graphics.views;

import org.game.GraphicLogger;
import org.game.Player;
import org.graphics.controllers.ModelListener;
import org.graphics.controllers.ResizeListener;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private PlayerPanelView whitePlayerPanelView;
    private PlayerPanelView blackPlayerPanelView;

    private BoardView boardView;

    //todo: a panelek átfaktorálása a gombok számára
    public GamePanel() {
        this.setLayout(new BorderLayout());
        boardView = new BoardView();
        whitePlayerPanelView = new PlayerPanelView();
        blackPlayerPanelView = new PlayerPanelView();

        blackPlayerPanelView.setBorder(BorderFactory.createLineBorder(Color.red));
        whitePlayerPanelView.setBorder(BorderFactory.createLineBorder(Color.red));
        boardView.setBorder(BorderFactory.createLineBorder(Color.red));

        this.add(blackPlayerPanelView, BorderLayout.NORTH);
        this.add(boardView, BorderLayout.CENTER);
        this.add(whitePlayerPanelView, BorderLayout.SOUTH);

        setVisible(true);
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
