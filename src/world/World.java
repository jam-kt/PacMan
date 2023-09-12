package world;

import entity.Dot;
import entity.Entity;
import entity.MovingEntity;
import entity.PacMan;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class World { // a collection of Tiles that are either a wall, gate, or path tile
    private final GamePanel gamePanel;
    private final BufferedImage[] tileImages = new BufferedImage[3];
    private final Tile[][] tileMap;
    private final HashSet<MovingEntity> movingEntities = new HashSet<>();
    private final HashSet<Entity> backgroundEntities = new HashSet<>(); //use for dots and powerups, simplifies render order
    private final int tileWidth;
    private final int tileHeight;


    public World(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.tileWidth = gamePanel.getPanelPixelWidth() / gamePanel.getTileSize();
        this.tileHeight = gamePanel.getPanelPixelHeight() / gamePanel.getTileSize();

        this.tileMap = new Tile[tileWidth][tileHeight];
        getTileImages();
        getMapData();
        initiateEntities();
    }

    public List<MovingEntity> getEntityList() {
        return movingEntities.stream().toList();
    }

    public List<Entity> getBackgroundEntities() {
        return backgroundEntities.stream().toList();
    }

    private Tile getTile(Point pixelPoint) { // retrieves a map Tile given pixel coordinates in form of a Point
        return tileMap[pixelPoint.x / gamePanel.getTileSize()][pixelPoint.y / gamePanel.getTileSize()];
    }

    public void updateMovingEntities() {
        List<MovingEntity> entityList = this.getEntityList();
        for (MovingEntity entity : entityList) {
            entity.update();
        }
    }

    public void drawEntities(Graphics2D graphics2D) {
        List<Entity> backgroundEntities = this.getBackgroundEntities();
        for (Entity entity : backgroundEntities) {
            entity.draw(graphics2D, gamePanel.getTileSize(), gamePanel.getTileSize());
        }

        List<MovingEntity> entityList = this.getEntityList(); // allows more important entities (moving ones) to be drawn on top
        for (Entity entity : entityList) {
            entity.draw(graphics2D, gamePanel.getTileSize(), gamePanel.getTileSize());
        }
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

    /** called once at instantiation, fills tileMap[][] with Tiles according to data in a .txt file. Also fills any path
     * tile with a Dot entity
     */
    private void getMapData() {
        try{
            InputStream inputStream = getClass().getResourceAsStream("/maps/DefaultMap.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            for (int y = 0; y < tileHeight; y++) {
                String line = bufferedReader.readLine();
                String[] numberLine = line.split(" ");

                for (int x = 0; x < tileWidth; x++) {
                    int tileNum = Integer.parseInt(numberLine[x]); // use for instantiation
                    tileMap[x][y] = new Tile(new Point(x * gamePanel.getTileSize(), y * gamePanel.getTileSize()),
                            tileImages[tileNum], (tileNum == 0) || (tileNum == 2));

                    if(tileNum == 1) { // to spawn a Dot entity where there are path tiles
                        Dot tempDot = new Dot(tileMap[x][y].getPixelPoint());
                        this.backgroundEntities.add(tempDot);
                        tileMap[x][y].addOccupant(tempDot);
                    }
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

    private void initiateEntities() {
        PacMan pac = new PacMan(gamePanel,this,new Point(2 * gamePanel.getTileSize(), 2 * gamePanel.getTileSize()));
        this.movingEntities.add(pac);
        this.getTile(pac.getPosition()).addOccupant(pac);

    }

    private List<Tile> get3x3Tiles(Point pixelPoint) {
        List<Tile> outList = new ArrayList<>(9);
        Tile center = getTile(pixelPoint);
        return outList;
    }
    private boolean willCollide(MovingEntity entity, Point intendedDestination) { // helper to moveEntity
        Tile intendedTile = getTile(intendedDestination);
        if(intendedTile.isWall()) {
            Rectangle tileHitbox = new Rectangle(intendedTile.getPixelPoint().x, intendedTile.getPixelPoint().y, gamePanel.getTileSize(), gamePanel.getTileSize());
            return entity.getHitbox().intersects(tileHitbox);
        }
        return false;
    }

    /**
     * Entities must call this method to finalize movement in the World. Do not change an Entity's position from its own
     * class. Assumes that the intendedDestination is in a valid tile. Entities should call willCollide to verify before
     * initiating a move.
     */
    public boolean moveEntity(MovingEntity entity, Point intendedDestination) { // entities must call this method to finalize movement in the World
        if(willCollide(entity, intendedDestination)) {
            return false;
        }
        else {
            getTile(entity.getPosition()).removeOccupant(entity);
            getTile(intendedDestination).addOccupant(entity);
            entity.setPosition(intendedDestination);
            return true;

        }
    }

}
