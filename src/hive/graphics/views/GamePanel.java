package hive.graphics.views;

import hive.GraphicLogger;
import hive.game.Player;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private final PlayerPanelView whitePlayerPanelView;
    private final PlayerPanelView blackPlayerPanelView;

    private final BoardView boardView;

    public GamePanel() {
        this.setLayout(new BorderLayout());
        boardView = new BoardView();
        whitePlayerPanelView = new PlayerPanelView();
        blackPlayerPanelView = new PlayerPanelView();

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
