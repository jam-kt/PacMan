package entity;

import main.GamePanel;
import world.Point;
import main.KeyHandler;
import world.World;

import java.awt.*;
import java.util.List;


public class PacMan implements MovingEntity {
    private final GamePanel gp;
    private final World world;
    private final KeyHandler keyHandler;
    private Point position;
    private final AnimationHandler animationHandler;
    private final int speed;
    private String currentDirection = "right";
    private final Rectangle hitbox;

    public PacMan(GamePanel gamePanel, World world, Point position, int SpeedTilesPerSec) {
        this.position = position;
        this.gp = gamePanel;
        this.keyHandler = gp.keyHandler;
        this.speed = (gp.getTileSize() * SpeedTilesPerSec) / 60; // what is his speed? Right now, 8 tiles per second
        this.world = world;
        this.animationHandler = new AnimationHandler(this, 5);
        this.hitbox = new Rectangle(0, 0, gp.getTileSize(), gp.getTileSize());

    }

    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public Point getPosition() {
        return this.position;
    }

    @Override
    public Rectangle getCurrentHitbox() { // use for getting the current hitbox of the entity
        this.hitbox.setLocation(this.position.x, this.position.y);
        return this.hitbox;
    }

    public Rectangle getIntendedHitbox(Point intendedPoint) {// returns the hitbox for the entity if it were to be moved to intendedPoint
        return new Rectangle(intendedPoint.x, intendedPoint.y, gp.getTileSize(), gp.getTileSize());
    }

    public String getCurrentDirection() {
        return this.currentDirection;
    }

    public void move() {
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

    public void checkInteractions() {
        List<Entity> sharingATile = world.getTile(this.position).getOccupants();
        for(Entity entity : sharingATile) {
            switch (entity.getClass().getName()) {
                case "entity.Dot":
                    if(this.getCurrentHitbox().intersects(entity.getCurrentHitbox())) {
                        world.removeEntity(entity);
                        //increase score with gameflow manager type class TODO
                    }
            }
        }
    }

    public void update() {
        this.move();
        this.checkInteractions();
    }

    public void draw(Graphics2D graphics2D, int width, int height) {
        graphics2D.drawImage(animationHandler.getNextFrame(), position.x, position.y, width, height, null);
    }

}
