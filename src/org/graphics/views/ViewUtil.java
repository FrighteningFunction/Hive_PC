package org.graphics.views;

import org.game.Coordinate;
import org.game.GameTile;

import javax.swing.*;

public class ViewUtil {
    private ViewUtil(){}

    public static <T extends JComponent> Coordinate refactorCoordinate(Coordinate c, T container) {
        double w = container.getWidth();
        double h = container.getHeight();

        //Azért toljuk el a radiussal és a magassággal, mert a hexatile-t középre akarjuk
        double x = w / 2 + c.getX()-GameTile.getTileRadius();
        double y = h / 2 - c.getY()-GameTile.getHexaTileHeight()/2;

        return new Coordinate(x, y);
    }
}
