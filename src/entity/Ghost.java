package entity;

import main.GamePanel;
import world.Point;
import world.World;

import java.awt.*;

public abstract class Ghost implements MovingEntity {
    private final GamePanel gamePanel;
    private final World world;
    private Point position;
    private final AnimationHandler animationHandler;
    private final int speed;
    private String currentDirection = "right";
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

    }

    @Override
    public Rectangle getCurrentHitbox() {
        return this.hitbox;
    }

    @Override
    public Rectangle getIntendedHitbox(Point intendedPoint) {
        return new Rectangle(intendedPoint.x, intendedPoint.y, gamePanel.getTileSize(), gamePanel.getTileSize());
    }

    @Override
    public void checkInteractions() {
        if(world.getTile(this.position).containsOccupantType(PacMan.class)) {
            //end game TODO
        }
    }

    @Override
    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public void move() {

    }
}
