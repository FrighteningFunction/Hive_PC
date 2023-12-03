package hive.graphics;

import hive.graphics.views.TileView;
import hive.graphics.controllers.ModelListener;

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
