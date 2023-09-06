package main;

import entity.PacMan;
import world.Point;

import javax.swing.JPanel;
import java.awt.*;

public class GamePanel extends JPanel{

    // 224x288 original resolution (28 cols 36 rows of tiles (8x8))
    private final int retroTileSize = 8; // 8x8 tiles
    private final int scale = 2;
    public final int tileSize = retroTileSize * scale;  // 16x16 tiles
    private final int panelWidth = tileSize * 28; // 28 columns
    private final int panelHeight = tileSize * 36; // 36 rows

    private final KeyHandler keyHandler = new KeyHandler();
    PacMan pacMan = new PacMan(this, keyHandler, new Point(100, 100), tileSize);

    public GamePanel() {
        this.setPreferredSize(new Dimension(panelWidth, panelHeight));
        this.setBackground(Color.BLUE);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void update() { // executes game logic for entities within eventScheduler such as movement/collisions
        pacMan.update();
    }

    public void paintComponent(Graphics g) { // renders background and all entities within eventScheduler
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;
        pacMan.draw(graphics2D);
        graphics2D.dispose();
    }
}
