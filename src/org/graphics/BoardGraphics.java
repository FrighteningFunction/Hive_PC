package org.graphics;

import org.game.Coordinate;
import org.game.GameBoard;
import org.game.GameTile;

import javax.swing.*;
import java.awt.*;

public class BoardGraphics extends JPanel {

    private static BoardGraphics instance;
    private GameBoard board = GameBoard.getInstance();

    private BoardGraphics() {
        this.setLayout(null);
    }

    public static BoardGraphics getInstance(){
        if(instance==null){
            instance = new BoardGraphics();
        }
        return instance;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (GameTile tile : board.getInitializedTileSet()) {
            tile.repaint();
        }
    }

    public Coordinate refactorCoordinate(Coordinate c) {
        double w = this.getWidth();
        double h = this.getHeight();

        double x = w / 2 + c.getX();
        double y = h / 2 - c.getY();

        return new Coordinate(x, y);
    }


}
