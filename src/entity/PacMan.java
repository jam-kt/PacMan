package entity;

import world.Point;
import main.GamePanel;
import main.KeyHandler;

import java.awt.*;


public class PacMan implements MovingEntity {
    private final GamePanel gamePanel;
    private final KeyHandler keyHandler;
    private final Point position;
    private final AnimationHandler animationHandler;
    private final int speed;
    private String currentDirection = "up";
    private int futureDirection; // stores a direction that pacman will take once at an appropriate spot

    public PacMan(GamePanel gamePanel, KeyHandler keyHandler, Point position, int tileSize) {
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        this.position = position;
        this.speed = (tileSize * 11) / 60; // what is his speed? Right now, 11 tiles per second

        this.animationHandler = new AnimationHandler(this, 10);
    }

    @Override
    public Point getPosition() {
        return this.position;
    }

    public String getCurrentDirection() {
        return this.currentDirection;
    }

    private void move() { // need to change to add collision detection and use future/current directions
        if(keyHandler.w) {
            position.y -= speed;
            currentDirection = "up";
        }
        else if(keyHandler.s) {
            position.y += speed;
            currentDirection = "down";
        }
        else if(keyHandler.a) {
            position.x -= speed;
            currentDirection = "left";
        }
        else if(keyHandler.d) {
            position.x += speed;
            currentDirection = "right";
        }
    }

    public void update() {
        this.move();
    }

    public void draw(Graphics2D graphics2D) {
        graphics2D.drawImage(animationHandler.getNextFrame(), position.x, position.y,
                gamePanel.getTileSize(), gamePanel.getTileSize(), null);
    }

}
