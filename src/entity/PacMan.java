package entity;

import main.GamePanel;
import world.Point;
import main.KeyHandler;
import world.World;

import java.awt.*;


public class PacMan implements MovingEntity {
    private final GamePanel gp;
    private final World world;
    private final KeyHandler keyHandler;
    private Point position;
    private final AnimationHandler animationHandler;
    private final int speed;
    private String currentDirection = "up";
    private String futureDirection = "up"; // stores a direction that pacman will take once at an appropriate spot
    private final Rectangle hitbox;

    public PacMan(GamePanel gamePanel, World world, Point position) {
        this.gp = gamePanel;
        this.keyHandler = gp.keyHandler;
        this.position = position;
        this.speed = 1; //(gp.getTileSize() * 11) / 60; // what is his speed? Right now, 11 tiles per second
        this.world = world;
        this.animationHandler = new AnimationHandler(this, 10);
        this.hitbox = new Rectangle(2*gp.getScale(), 2*gp.getScale(), 12*gp.getScale(), 12*gp.getScale());

    }

    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public Point getPosition() {
        return this.position;
    }

    @Override
    public Rectangle getHitbox() {
        this.updateHitbox();
        return this.hitbox;
    }

    private void updateHitbox() { // the rectangle for hitbox is stationary, so it must be manually moved to entity's current pos
        this.hitbox.setLocation(this.position.x + 2*gp.getScale(), this.position.y + 2*gp.getScale());
    }

    public String getCurrentDirection() {
        return this.currentDirection;
    }

    private void move() {
        String intendedDirection = this.currentDirection;
        if(keyHandler.w) {
            intendedDirection = "up";
        }
        else if(keyHandler.s) {
            intendedDirection = "down";
        }
        else if(keyHandler.a) {
            intendedDirection = "left";
        }
        else if(keyHandler.d) {
            intendedDirection = "right";
        }

        if(!tryMovement(intendedDirection)) { // if moving in our intended direction fails, try to move in our current direction
            tryMovement(this.currentDirection);
        }
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

    public void update() {
        this.move();
    }

    public void draw(Graphics2D graphics2D, int width, int height) {
        graphics2D.drawImage(animationHandler.getNextFrame(), position.x, position.y, width, height, null);
    }

}
