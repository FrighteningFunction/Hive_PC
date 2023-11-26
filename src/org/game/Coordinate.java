package org.game;

import static java.lang.Math.*;

public class Coordinate {
    private final double x;
    private final double y;
    private static final double XGRID = GameTile.getHeight() * cos(GameTile.getDir());
    private static final double YGRID = GameTile.getHeight() * sin(GameTile.getDir());

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public static double getXDELTA() {
        return XGRID;
    }

    public static double getYDELTA() {
        return YGRID;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Coordinate that = (Coordinate) obj;

        return Math.round(x / XGRID) == Math.round(that.x / XGRID) && Math.round(y / YGRID) == Math.round(that.y / YGRID);
    }

    @Override
    public int hashCode() {
        int hashX = (int) Math.round(x / XGRID);
        int hashY = (int) Math.round(y / YGRID);
        return 31 * hashX + hashY;
    }
}
