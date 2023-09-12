package entity;

import world.Point;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Dot implements Entity{
    private Point position;
    private BufferedImage image;

    public Dot(Point position) {
        this.position = position;
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/spriteFrames/dot.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Point getPosition() {
        return null;
    }
    @Override
    public void draw(Graphics2D graphics2D, int width, int height) {
        graphics2D.drawImage(this.image, position.x, position.y, width, height, null);
    }
}
