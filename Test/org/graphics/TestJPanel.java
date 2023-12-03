package org.graphics;

import org.graphics.controllers.ModelListener;
import org.graphics.views.TileView;

import javax.swing.*;

public class TestJPanel extends JPanel implements TileView {

    public TestJPanel(){
        setLayout(null);
        setBounds(350,470, 700, 840);
        this.setVisible(true);
    }

    @Override
    public void addListener(ModelListener listener) {
        throw new UnsupportedOperationException();
    }
}
