package main;

import world.World;
import javax.swing.JPanel;
import java.awt.*;

public class GamePanel extends JPanel{

    // 224x288 original resolution (28 cols 36 rows of tiles (8x8))
    private final int retroTileSize = 8; // 8x8 tiles
    private final int scale = 3;
    private final int tileSize = retroTileSize * scale;  // 16x16 tiles
    private final int panelPixelWidth = tileSize * 28; // 28 columns
    private final int panelPixelHeight = tileSize * 31; // 36 rows (31 without extra GUI parts like lives/score)
    public final KeyHandler keyHandler = new KeyHandler();

    World world = new World(this);

    public GamePanel() {
        this.setPreferredSize(new Dimension(panelPixelWidth, panelPixelHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public int getScale() {
        return this.scale;
    }

    public int getPanelPixelHeight() {
        return panelPixelHeight;
    }

    public int getPanelPixelWidth() {
        return panelPixelWidth;
    }

    public int getTileSize() {
        return tileSize;
    }

    public void update() { // executes game logic for entities within eventScheduler such as movement/collisions
        world.updateMovingEntities();
    }

    public void paintComponent(Graphics g) { // renders background and all entities within eventScheduler
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;

        world.drawBackground(graphics2D);
        world.drawEntities(graphics2D);

        graphics2D.dispose();
    }
}
