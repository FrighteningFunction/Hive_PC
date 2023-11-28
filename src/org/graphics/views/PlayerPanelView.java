package org.graphics.views;

import org.game.Coordinate;
import org.game.GraphicLogger;
import org.game.Player;

import javax.swing.*;
import java.awt.*;

public class PlayerPanelView extends JPanel implements NullPanel{

    public PlayerPanelView(){
        this.setLayout(null);
        GraphicLogger.getLogger().info("PlayerPanelView created successfully.");
    }

    //todo: megvalósítás
    @Override
    protected void paintComponent(Graphics g){
        throw new UnsupportedOperationException();
    }

    @Override
    public Coordinate refactorCoordinate(Coordinate c) {
        double w = this.getWidth();
        double h = this.getHeight();

        double x = w / 2 + c.getX();
        double y = h / 2 - c.getY();

        return new Coordinate(x, y);
    }
}
