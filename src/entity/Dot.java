package entity;

import world.Point;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Dot implements Entity{
    private Point position;
    private BufferedImage image;
    private final Rectangle hitbox;

    public Dot(Point position, int tileSize) {
        this.setPosition(position);
        int temp = (tileSize * 3) / 8;
        this.hitbox = new Rectangle(position.x + temp, position.y + temp, tileSize - (2*temp), tileSize - (2*temp));
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/spriteFrames/dot.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Point getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public void draw(Graphics2D graphics2D, int width, int height) {
        graphics2D.drawImage(this.image, position.x, position.y, width, height, null);
    }

    @Override
    public Rectangle getCurrentHitbox() {
        return this.hitbox;
    }
}
