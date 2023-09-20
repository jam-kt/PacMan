package entity;

import main.GamePanel;
import world.Point;
import world.World;

public class Pinky extends Ghost {
    public Pinky(GamePanel gamePanel, World world, Point position, int SpeedTilesPerSec) {
        super(gamePanel, world, position, SpeedTilesPerSec);
    }

    @Override
    public Point getMyTarget(GamePanel gamePanel, World world) {
       Point pacPosition = Point.scaleDown(world.pacMan.getPosition(), gamePanel.getTileSize());
       String pacDirection = world.pacMan.getCurrentDirection();
       Point myTarget = pacPosition;
        switch (pacDirection) {
            case "up" -> myTarget = new Point(pacPosition.x, pacPosition.y - 4);
            case "down" -> myTarget = new Point(pacPosition.x, pacPosition.y + 4);
            case "left" -> myTarget = new Point(pacPosition.x - 4, pacPosition.y);
            case "right" -> myTarget = new Point(pacPosition.x + 4, pacPosition.y);
        }
        if(world.tileInBound(myTarget)) {
            return myTarget;
        }
        return pacPosition;
    }
}
