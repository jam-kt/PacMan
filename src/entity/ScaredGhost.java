package entity;

import main.GamePanel;
import world.Point;
import world.World;

import java.awt.*;
import java.util.Random;

public class ScaredGhost implements MovingEntity{ // cannot extend Ghost, too many behaviors and interactions are different
    public final String myType; // a string representing the class name of the ghost this ScaredGhost was transformed from
    private final GamePanel gamePanel;
    private final World world;
    private Point position;
    private final AnimationHandler animationHandler;
    private final int speed;
    private String currentDirection;
    private String startingDirection;
    private final Rectangle hitbox;

    public ScaredGhost(GamePanel gamePanel, World world, Point position, int speedTilesPerSec, String myType, String currentDirection) {
        this.gamePanel = gamePanel;
        this.world = world;
        this.position = position;
        this.speed = (this.gamePanel.getTileSize() * speedTilesPerSec) / 60;
        this.myType = myType;
        this.currentDirection = reverseDirection(currentDirection);
        this.startingDirection = reverseDirection(currentDirection);
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
        if(this.getCurrentHitbox().intersects(world.pacMan.getCurrentHitbox())) {
            world.addEntity(new FloatyEyes(gamePanel, world, this.position, 9, this.myType, this.currentDirection));
            world.removeEntity(this);
        }
    }

    @Override
    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public void move() { // tries to move in a random direction that is not its current direction
        if(this.startingDirection != null) { // try to initiate movement by going in the opposite direction of original ghost (given as argument)
            tryMovement(this.startingDirection);
            this.startingDirection = null;
            return;
        }
        String intendedDirection = findRandomDirection();
        if(!tryMovement(intendedDirection)) {
            tryMovement(currentDirection);
        }
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

    private static String reverseDirection(String direction) {
        switch (direction) {
            case "up" -> {
                return "down";
            }
            case "down" -> {
                return "up";
            }
            case "left" -> {
                return "right";
            }
            case "right" -> {
                return "left";
            }
        }
        return direction;
    }

    private String findRandomDirection() {
        String intendedDirection = null;
        Random random = new Random();
        while(intendedDirection == null || intendedDirection.equals(reverseDirection(this.currentDirection))) {
            switch(random.nextInt(4)) {
                case 0 -> intendedDirection = "up";
                case 1 -> intendedDirection = "down";
                case 2 -> intendedDirection = "left";
                case 3 -> intendedDirection = "right";
            }
        }
        return intendedDirection;
    }

}
