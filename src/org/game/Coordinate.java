package org.game;

public class Coordinate {
    private double x;
    private double y;
    private static final double DELTA = 0.01; // Define the acceptable delta

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Coordinate that = (Coordinate) obj;

        return Math.abs(x - that.x) < DELTA && Math.abs(y - that.y) < DELTA;
    }

    @Override
    public int hashCode() {
        // Implement a suitable hash function considering DELTA
        long hashX = Double.doubleToLongBits(x);
        long hashY = Double.doubleToLongBits(y);
        return 31 * Long.hashCode(hashX) + Long.hashCode(hashY);
    }
}
