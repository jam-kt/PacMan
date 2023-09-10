package world;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Background { // a collection of Tiles that are either a wall, gate, or path tile
    private final GamePanel gamePanel;
    private BufferedImage[] tileImages;
    private Tile tileMap[][];
    private int tileWidth;
    private int tileHeight;

    public Background(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.tileWidth = gamePanel.getPanelPixelWidth() / gamePanel.getTileSize();
        this.tileHeight = gamePanel.getPanelPixelHeight() / gamePanel.getTileSize();

        this.tileImages = new BufferedImage[3];
        this.tileMap = new Tile[tileWidth][tileHeight];

        getTileImages();
        getMapData();
    }

    private void getTileImages() {
        try {
            tileImages[0] = ImageIO.read(getClass().getResourceAsStream("/spriteFrames/wall.png"));
            tileImages[1] = ImageIO.read(getClass().getResourceAsStream("/spriteFrames/path.png"));
            tileImages[2] = ImageIO.read(getClass().getResourceAsStream("/spriteFrames/gate.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getMapData() { // called once at instantiation, fills tileMap[][] with Tiles according to data in a .txt file
        try{
            InputStream inputStream = getClass().getResourceAsStream("/maps/DefaultMap.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            for (int y = 0; y < tileHeight; y++) {
                String line = bufferedReader.readLine();
                String[] numberLine = line.split(" ");
                for (int x = 0; x < tileWidth; x++) {
                    int tileNum = Integer.parseInt(numberLine[x]); // use for instantiation
                    tileMap[x][y] = new Tile(new Point(x, y), tileImages[tileNum], (tileNum == 0));
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawBackground(Graphics2D graphics2D) { // draws every background tile image when called. Call before entities are drawn
        for (int y = 0; y < tileHeight; y++) {
            for (int x = 0; x < tileWidth; x++) {
                graphics2D.drawImage(tileMap[x][y].getImage(),
                        x * gamePanel.getTileSize(),
                        y * gamePanel.getTileSize(),
                        gamePanel.getTileSize(),
                        gamePanel.getTileSize(),
                        null);
            }
        }
    }

}
