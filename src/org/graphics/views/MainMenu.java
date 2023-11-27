package org.graphics.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {
    JButton newGame = new JButton("New game");

    JButton exit = new JButton ("exit");

    MenuButtonsPressed menuListener = new MenuButtonsPressed();

    private final int ROWS = 2;

    JPanel mainPanel = new JPanel();

    public MainMenu(){
        this.setLayout(new GridBagLayout());
        this.add(mainPanel);
        newGame.setActionCommand("newGame");
        newGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit.setActionCommand("exit");
        exit.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(newGame);
        mainPanel.add(exit);

        newGame.addActionListener(menuListener);
        exit.addActionListener(menuListener);
    }

    class MenuButtonsPressed implements ActionListener {
        public void actionPerformed(ActionEvent e){
            String c = e.getActionCommand();
            switch (c){
                case "newGame":
                    throw new UnsupportedOperationException();
                case "exit":
                    System.exit(0);
            }
        }
    }
}
