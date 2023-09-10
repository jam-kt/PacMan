package world;

import entity.Entity;

import java.awt.image.BufferedImage;
import java.util.HashSet;

public class Tile {
    private BufferedImage image;
    private HashSet<Entity> occupants; // = new HashSet<>(); // default capacity of 16
    private boolean isWall;
    private Point pixelPoint; // represents the pixel coordinates for the upper-left most pixel in the tile

    public Tile(Point point, BufferedImage image, boolean isWall) {
        this.image = image;
        this.isWall = isWall;
        this.pixelPoint = point;
    }

    public BufferedImage getImage() {
        return image;
    }

    public boolean isWall() {
        return this.isWall;
    }

    public Point getPixelPoint() {
        return pixelPoint;
    }
}
