package hive.game;

import static java.lang.Math.*;

/**
 * A játékban a játékbéli valamint a pixel-koordináták összetartásáért felelős osztály.
 */
public class Coordinate {
    private final double x;
    private final double y;

    /**
     * A "négyzetrács"-ot definiáló x érték. Ekkora távolságra lehetnek egymástól  hexatile-ok
     * a játék elrendezése szerint.
     */
    private static final double XGRID = GameTile.getHexaTileHeight() * cos(GameTile.getDir());

    /**
     * A rácsot definiáló x érték. Ekkora távolságra lehetnek egymástól a hexatile-ok.
     */
    private static final double YGRID = GameTile.getHexaTileHeight() * sin(GameTile.getDir());

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

    public static double getXGRID() {
        return XGRID;
    }

    public static double getYGRID() {
        return YGRID;
    }

    /**
     * Overrided equals metódus. Két koordináta egyenlő, ha mindkettőjük számára ugyanaz a rácspont van a legközelebb.
     * @param obj összehasonlítandó Coordinate
     * @return igaz ha egyenlőek, hamis ha nem
     */
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
