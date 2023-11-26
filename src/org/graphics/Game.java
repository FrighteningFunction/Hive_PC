package org.graphics;

import org.game.GameLogic;

import javax.swing.*;

public class Game extends JPanel {
    PlayerPanel whitePlayerPanel;
    PlayerPanel blackPlayerPanel;

    BoardGraphics boardGraphics;

    GameLogic gameLogic;

    public Game(){
        GameLogic.newGame();

    }
}
