package world;

import entity.*;
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

public class World { //represents the game level with tiles and contains, updates, and renders all entities
    private final GamePanel gamePanel;
    private final BufferedImage[] tileImages = new BufferedImage[3];
    private final Tile[][] tileMap;
    private final HashSet<MovingEntity> movingEntities = new HashSet<>();
    private final HashSet<Entity> backgroundEntities = new HashSet<>(); //use for dots and powerups, simplifies render order
    private final int tileWidth;
    private final int tileHeight;
    public final GameplayManager gameManager;

    public PacMan pacMan; // so much easier to keep pac as an attribute of World. Circumvent with a findEntity method


    public World(GamePanel gamePanel, GameplayManager gameManager) {
        this.gamePanel = gamePanel;
        this.gameManager = gameManager;
        this.tileWidth = gamePanel.getPanelPixelWidth() / gamePanel.getTileSize();
        this.tileHeight = gamePanel.getPanelPixelHeight() / gamePanel.getTileSize();

        this.tileMap = new Tile[tileWidth][tileHeight];
        getTileImages();
        getMapData();
        initiateEntities();
    }

    public List<MovingEntity> getMovingEntities() {
        return movingEntities.stream().toList();
    }

    public List<Entity> getBackgroundEntities() {
        return backgroundEntities.stream().toList();
    }

    public Tile getTile(Point pixelPoint) { // retrieves a map Tile given pixel coordinates in form of a Point
        return tileMap[pixelPoint.x / gamePanel.getTileSize()][pixelPoint.y / gamePanel.getTileSize()];
    }

    public Tile getTileFromMap(int mapX, int mapY) { // retrieves a map Tile given the map coordinates
        return tileMap[mapX][mapY];
    }

    public void updateMovingEntities() {
        List<MovingEntity> entityList = this.getMovingEntities();
        for (MovingEntity entity : entityList) {
            entity.update();
        }
    }

    public void drawEntities(Graphics2D graphics2D) {
        List<Entity> backgroundEntities = this.getBackgroundEntities();
        for (Entity entity : backgroundEntities) {
            entity.draw(graphics2D, gamePanel.getTileSize(), gamePanel.getTileSize());
        }

        List<MovingEntity> entityList = this.getMovingEntities(); // allows more important entities (moving ones) to be drawn on top
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
                        this.addEntity(new Dot(tileMap[x][y].getPixelPoint(), gamePanel.getTileSize()));
                        gameManager.increaseDotCount();
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
                        x * gamePanel.getTileSize() + 4,
                        y * gamePanel.getTileSize() + 4,
                        gamePanel.getTileSize() - 8,
                        gamePanel.getTileSize() - 8,
                        null);
            }
        }
    }

    private void initiateEntities() {
        this.pacMan = new PacMan(gamePanel,this, tileMap[13][17].getPixelPoint(), 8);
        this.addEntity(this.pacMan);
        this.addEntity(new Pinky(this.gamePanel, this, tileMap[13][11].getPixelPoint(), 8));

    }

    private List<Tile> get3x3Tiles(Point pixelPoint) {
        List<Tile> outList = new ArrayList<>(8);
        outList.add(getTile(new Point(pixelPoint.x - gamePanel.getTileSize(), pixelPoint.y - gamePanel.getTileSize())));
        outList.add(getTile(new Point(pixelPoint.x + gamePanel.getTileSize(), pixelPoint.y + gamePanel.getTileSize())));
        outList.add(getTile(new Point(pixelPoint.x - gamePanel.getTileSize(), pixelPoint.y + gamePanel.getTileSize())));
        outList.add(getTile(new Point(pixelPoint.x + gamePanel.getTileSize(), pixelPoint.y - gamePanel.getTileSize())));
        outList.add(getTile(new Point(pixelPoint.x, pixelPoint.y - gamePanel.getTileSize())));
        outList.add(getTile(new Point(pixelPoint.x, pixelPoint.y + gamePanel.getTileSize())));
        outList.add(getTile(new Point(pixelPoint.x - gamePanel.getTileSize(), pixelPoint.y)));
        outList.add(getTile(new Point(pixelPoint.x + gamePanel.getTileSize(), pixelPoint.y )));

        return outList;
    }

    private boolean willCollide(MovingEntity entity, Point intendedDestination) { // helper to moveEntity
        Tile intendedTile = getTile(intendedDestination);
        if (intendedTile.isWall()) {
            return true;
        }
        else {
            List<Tile> neighborTiles = get3x3Tiles(intendedDestination);
            for(Tile tile : neighborTiles) {
                if(tile.isWall()) {
                    Rectangle tileHitbox = new Rectangle(tile.getPixelPoint().x,
                            tile.getPixelPoint().y, gamePanel.getTileSize(), gamePanel.getTileSize());

                    if(tileHitbox.intersects(entity.getIntendedHitbox(intendedDestination))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Entities must call this method to try movement in the World. Do not change an Entity's position from its own
     * class.
     */
    public boolean moveEntity(MovingEntity entity, Point intendedDestination) {
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

    public void removeEntity(Entity entity) {
        this.getTile(entity.getPosition()).removeOccupant(entity);

        if(backgroundEntities.remove(entity)) {
            return;
        }
        else {
            movingEntities.remove((MovingEntity) entity);
        }
    }

    public boolean addEntity(Entity entity) {
        if(this.getTile(entity.getPosition()).isWall()) {
            return false;
        }
        if(entity instanceof MovingEntity) {
            this.movingEntities.add((MovingEntity) entity);
        }
        else {
            this.backgroundEntities.add(entity);
        }
        this.getTile(entity.getPosition()).addOccupant(entity);
        return true;
    }

    public boolean tileInBound(Point tilePoint) {
        boolean x = ((0 <= tilePoint.x) && (tilePoint.x <= (gamePanel.getPanelPixelWidth() / gamePanel.getTileSize())));
        boolean y = ((0 <= tilePoint.y) && (tilePoint.y <= (gamePanel.getPanelPixelHeight() / gamePanel.getTileSize())));
        return x && y;
    }

}
