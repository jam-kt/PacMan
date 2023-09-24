package entity;

import main.GamePanel;
import pathing.AStarPathingStrategy;
import pathing.PathingStrategy;
import world.Point;
import world.World;

import java.awt.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class FloatyEyes implements MovingEntity{
    public final String myType; // a string representing the class name of the ghost this dead ghost was created from
    private final GamePanel gamePanel;
    private final World world;
    private Point position;
    private final AnimationHandler animationHandler;
    private final int speed;
    private String currentDirection;
    private final Rectangle hitbox;

    public FloatyEyes(GamePanel gamePanel, World world, Point position, int speedTilesPerSec, String myType, String currentDirection) {
        this.gamePanel = gamePanel;
        this.world = world;
        this.position = position;
        this.speed = (this.gamePanel.getTileSize() * speedTilesPerSec) / 60;
        this.myType = myType;
        this.currentDirection = currentDirection;
        this.animationHandler = new AnimationHandler(this, 10);
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
        if(world.getTile(this.position).equals(world.getTileFromMap(13, 11))) {
            switch (this.myType) {
                case "entity.Blinky" -> this.world.addEntity(new Blinky(world.gamePanel, world, this.position, 7, "up"));
                case "entity.Clyde" -> this.world.addEntity(new Clyde(world.gamePanel, world, this.position, 7, "up"));
                case "entity.Inky" -> this.world.addEntity(new Inky(world.gamePanel, world, this.position, 7, "up"));
                case "entity.Pinky" -> this.world.addEntity(new Pinky(world.gamePanel, world, this.position, 7, "up"));
            }
            world.removeEntity(this);
        }
    }

    @Override
    public void setPosition(Point position) {
        this.position = position;
    }

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
        String intendedDirection = this.currentDirection;
        java.util.List<Point> path = getPath(Point.scaleDown(world.getTileFromMap(13, 11).getPixelPoint(), gamePanel.getTileSize()));
        if(!path.isEmpty()) {
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

    private boolean tryMovement(String direction) { // returns true if able to travel in this direction, updates position/direction
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
