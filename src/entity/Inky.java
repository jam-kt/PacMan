package entity;

import main.GamePanel;
import world.Point;
import world.World;

public class Inky extends Ghost {

    public Inky(GamePanel gamePanel, World world, Point position, int SpeedTilesPerSec, String currentDirection) {
        super(gamePanel, world, position, SpeedTilesPerSec, currentDirection);
    }

    @Override
    public Point getMyTarget(GamePanel gamePanel, World world) {
        Point pacPosition = Point.scaleDown(world.pacMan.getPosition(), gamePanel.getTileSize());
        String pacDirection = world.pacMan.getCurrentDirection();
        Point myTarget = pacPosition;
        switch (pacDirection) { // two tiles ahead of pac, or if pac is facing up, two tiles up & two left
            case "up" -> myTarget = new Point(pacPosition.x - 2, pacPosition.y - 2);
            case "down" -> myTarget = new Point(pacPosition.x, pacPosition.y + 2);
            case "left" -> myTarget = new Point(pacPosition.x - 2, pacPosition.y);
            case "right" -> myTarget = new Point(pacPosition.x + 2, pacPosition.y);
        }
        // get Blinky's pos, then from myTarget create a vector to Blink's pos, rotate vector 180 and that is target
        Point blinkyTilePos = Point.scaleDown(world.getMovingEntity(entity.Blinky.class).getPosition(), gamePanel.getTileSize());
        int vectorX = blinkyTilePos.x - myTarget.x;
        int vectorY = blinkyTilePos.y - myTarget.y;
        
        myTarget.x = myTarget.x - vectorX;
        myTarget.y = myTarget.y - vectorY;

        return world.clampTile(myTarget);
    }
}