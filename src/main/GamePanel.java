package main;

import world.GameplayManager;
import world.World;
import javax.swing.JPanel;
import java.awt.*;

public class GamePanel extends JPanel{

    // 224x288 original resolution (28 cols 36 rows of tiles (8x8))
    private final int originalTileSize = 8; // A tile is minimum 8x8 pixels. All sprites are 16x16 however
    private final int scale = 3; // 2 or 3 recommended
    private final int tileSize = originalTileSize * scale;
    private final int panelPixelWidth = tileSize * 28; // 28 columns
    private final int panelPixelHeight = tileSize * 31; // 36 rows (31 without extra GUI parts like lives/score)
    public final KeyHandler keyHandler = new KeyHandler();

    private GameplayManager gameplayManager = new GameplayManager(this);
    private World world = new World(this, gameplayManager);


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

    public void update() { // executes game logic for entities within the world. Done through gameplayManager
        if(keyHandler.reset) {
            this.world = new World(this, new GameplayManager(this));
            keyHandler.reset = false;
            keyHandler.pause = true;
        }
        else if(!keyHandler.pause) {
            world.updateMovingEntities();
        }
    }

    public void paintComponent(Graphics g) { // renders background and all entities
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;

        world.drawBackground(graphics2D); // order matters! Background first
        world.drawEntities(graphics2D);

        graphics2D.dispose();
    }
}
