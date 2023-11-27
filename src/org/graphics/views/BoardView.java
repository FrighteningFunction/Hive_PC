package org.graphics.views;

import org.game.Coordinate;
import org.game.GameBoard;

import javax.swing.*;

public class BoardView extends JPanel {

    private static BoardView instance;
    private GameBoard board = GameBoard.getInstance();

    private BoardView() {
        this.setLayout(null);
    }

    public static BoardView getInstance(){
        if(instance==null){
            instance = new BoardView();
        }
        return instance;
    }

    public Coordinate refactorCoordinate(Coordinate c) {
        double w = this.getWidth();
        double h = this.getHeight();

        double x = w / 2 + c.getX();
        double y = h / 2 - c.getY();

        return new Coordinate(x, y);
    }
}
