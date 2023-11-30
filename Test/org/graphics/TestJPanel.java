package org.graphics;

import org.game.Coordinate;
import org.graphics.views.NullPanel;

import javax.swing.*;

public class TestJPanel extends JPanel implements NullPanel {

    public TestJPanel(){
        setLayout(null);
        setBounds(0,0, 700, 840);
        this.setVisible(true);
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
