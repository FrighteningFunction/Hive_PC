package org.graphics.views;

import org.game.Player;

import javax.swing.*;
import java.awt.*;

public class PlayerPanel extends JPanel {
    private Player p;

    public PlayerPanel(Player p){
        this.p = p;
    }

    @Override
    protected void paintComponent(Graphics g){
        throw new UnsupportedOperationException();
    }
}
