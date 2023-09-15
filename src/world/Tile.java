package world;

import entity.Entity;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Tile {
    private BufferedImage image;
    private HashSet<Entity> occupants = new HashSet<>(7); // default capacity of 16
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

    public void addOccupant(Entity entity) {
        this.occupants.add(entity);
    }

    public void removeOccupant(Entity entity) {
        this.occupants.remove(entity);
    }

    public boolean containsOccupantType(Class<?> classType) {
        for(Entity entity : this.occupants.stream().toList()) {
            if(entity.getClass().equals(classType)) {
                return true;
            }
        }
        return false;
    }

    public List<Entity> getOccupants() {
        return occupants.stream().toList();
    }
}
