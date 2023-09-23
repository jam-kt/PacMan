package entity;

import pathing.AStarPathingStrategy;
import pathing.PathingStrategy;
import main.GamePanel;
import world.Point;
import world.World;

import java.awt.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class Ghost implements MovingEntity {
    private final GamePanel gamePanel;
    private final World world;
    private Point position;
    private final AnimationHandler animationHandler;
    private final int speed;
    private String currentDirection = "up";
    private final Rectangle hitbox;

    public Ghost(GamePanel gamePanel, World world, Point position, int SpeedTilesPerSec) { // speed should be 8
        this.position = position;
        this.gamePanel = gamePanel;
        this.speed = (this.gamePanel.getTileSize() * SpeedTilesPerSec) / 60; // what is his speed? Right now, 8 tiles per second
        this.world = world;
        this.animationHandler = new AnimationHandler(this, 5);
        this.hitbox = new Rectangle(0, 0, this.gamePanel.getTileSize(), this.gamePanel.getTileSize());
    }

    @Override
    public Point getPosition() {
        return this.position;
    }

    @Override
    public void draw(Graphics2D graphics2D, int width, int height) {
        graphics2D.drawImage(animationHandler.getNextFrame(), position.x, position.y, width, height, null);
    }

    @Override
    public String getCurrentDirection() {
        return this.currentDirection;
    }

    @Override
    public void update() {
        this.move();
        this.checkInteractions();
    }

    @Override
    public Rectangle getCurrentHitbox() {
        this.hitbox.setLocation(this.position.x, this.position.y);
        return this.hitbox;
    }

    @Override
    public Rectangle getIntendedHitbox(Point intendedPoint) {
        return new Rectangle(intendedPoint.x, intendedPoint.y, gamePanel.getTileSize(), gamePanel.getTileSize());
    }

    @Override
    public void checkInteractions() {
//        if(world.pacMan.getCurrentHitbox().intersects(this.getCurrentHitbox())) {
//            world.gameManager.requestGameEnd();
//        }
    }

    @Override
    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * returns a point representing a tileMap coordinate. Use Point.scaleDown to turn pixelPoint into tile coord
     */
    public abstract Point getMyTarget(GamePanel gamePanel, World world); // each ghost has a unique target position relative to Pac

    private Function<Point, Stream<Point>> selectNeighborOptions(String currentDirection) { // to implement no 180 rule
        switch (currentDirection) {
            case "up" -> {
                return PathingStrategy.CARDINAL_NEIGHBORS_UP;
            }
            case "down" -> {
                return PathingStrategy.CARDINAL_NEIGHBORS_DOWN;
            }
            case "left" -> {
                return PathingStrategy.CARDINAL_NEIGHBORS_LEFT;
            }
            case "right" -> {
                return PathingStrategy.CARDINAL_NEIGHBORS_RIGHT;
            }
        }
        return PathingStrategy.CARDINAL_NEIGHBORS; // just so the compiler is happy. Shouldn't get here ever.
    }

    @Override
    public void move() {
        String intendedDirection;
        List<Point> path = getPath(this.getMyTarget(this.gamePanel, this.world));
        if(!path.isEmpty()) {
            Point pathSuggestedPoint = path.get(0);
            intendedDirection = Point.findDirectionFrom(Point.scaleDown(this.position, gamePanel.getTileSize()), pathSuggestedPoint);
        }
        else {
            path = getPath(Point.scaleDown(world.pacMan.getPosition(), gamePanel.getTileSize()));
            Point pathSuggestedPoint = path.get(0);
            intendedDirection = Point.findDirectionFrom(Point.scaleDown(this.position, gamePanel.getTileSize()), pathSuggestedPoint);
        }
        if(!tryMovement(intendedDirection)) { // if moving in our intended direction fails, try to move in our current direction
            tryMovement(this.currentDirection);
        }
    }

    private List<Point> getPath(Point targetPoint) {
        PathingStrategy pathingStrategy = new AStarPathingStrategy();

        return pathingStrategy.computePath(Point.scaleDown(this.position, gamePanel.getTileSize()),
                targetPoint,
                // since the path is computed at tile level, not pixel, we have to check map coordinates instead of pixel ones
                p -> !world.getTileFromMap(p.x, p.y).isWall(),
                Point::adjacent,
                selectNeighborOptions(this.currentDirection), gamePanel.getTileSize());

    }

    private boolean tryMovement(String direction) { // returns true if pac is able to travel in this direction, updates position/direction
        int intendedX = position.x;
        int intendedY = position.y;
        switch (direction) {
            case "up" -> intendedY -= speed;
            case "down" -> intendedY += speed;
            case "left" -> intendedX -= speed;
            case "right" -> intendedX += speed;
        }
        Point intendedPoint = new Point(intendedX, intendedY);
        if(world.moveEntity(this, intendedPoint)) {
            this.currentDirection = direction;
            return true;
        }
        else {
            return false;
        }
    }

}
