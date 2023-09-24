package entity;

import main.GamePanel;
import world.Point;
import world.World;

public class Blinky extends Ghost {

    public Blinky(GamePanel gamePanel, World world, Point position, int SpeedTilesPerSec, String currentDirection) {
        super(gamePanel, world, position, SpeedTilesPerSec, currentDirection);
    }

    @Override
    public Point getMyTarget(GamePanel gamePanel, World world) {
        return Point.scaleDown(world.pacMan.getPosition(), gamePanel.getTileSize()); // pac's tile position
    }
}
