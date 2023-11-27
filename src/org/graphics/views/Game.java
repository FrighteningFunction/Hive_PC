package org.graphics.views;

import org.game.GameBoard;
import org.game.GameLogic;
import org.game.Player;

import javax.swing.*;
import java.awt.*;

public class Game extends JPanel {
    private PlayerPanel whitePlayerPanel;
    private PlayerPanel blackPlayerPanel;

    private BoardView boardView;

    private GameLogic gameLogic;

    public Game(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
        this.setLayout(new GridLayout(3, 1));
        boardView = BoardView.getInstance();
        whitePlayerPanel = new PlayerPanel(gameLogic.getBlackPlayer());
        blackPlayerPanel = new PlayerPanel(gameLogic.getWhitePlayer());

        this.add(blackPlayerPanel);
        this.add(boardView);
        this.add(whitePlayerPanel);
    }

    public void spawnVictoryDialog(Player p) {
        JOptionPane.showMessageDialog(this, String.format("%s player won!", p.getColor()));
    }
}
