package main;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {

        JFrame gameWindow = new JFrame();
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.setResizable(false);
        gameWindow.setTitle("Procedural PacMan");
        gameWindow.setLocationRelativeTo(null);
        gameWindow.setVisible(true);

        GamePanel gamePanel = new GamePanel();
        gameWindow.add(gamePanel);
        gameWindow.pack();
        gamePanel.requestFocusInWindow();

        GameThread gameThread = new GameThread(gamePanel);

    }
}