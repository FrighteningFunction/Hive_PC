package org.graphics.views;

import org.game.Coordinate;
import org.game.GraphicLogger;

import javax.swing.*;
import java.awt.*;

public class PlayerPanelView extends JPanel implements NullPanel{

    public PlayerPanelView(){
        this.setLayout(null);
        setVisible(true);
        GraphicLogger.getLogger().info("PlayerPanelView created successfully.");
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
