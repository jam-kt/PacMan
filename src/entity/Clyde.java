package entity;

import main.GamePanel;
import world.Point;
import world.World;

public class Clyde extends Ghost {
    public Clyde(GamePanel gamePanel, World world, Point position, int SpeedTilesPerSec) {
        super(gamePanel, world, position, SpeedTilesPerSec);
    }

    private boolean isEightAway(Point pacPos, int tileSize) { // returns true if Clyde is currently more than 8 tiles away from pacman
        Point myPosition = Point.scaleDown(this.getPosition(), tileSize);
        int x = (int) Math.pow(pacPos.x - myPosition.x, 2);
        int y = (int) Math.pow(pacPos.y - myPosition.y, 2);
        return(Math.sqrt(x + y) >= 8);
    }

    @Override
    public Point getMyTarget(GamePanel gamePanel, World world) {
        Point pacPos = Point.scaleDown(world.pacMan.getPosition(), gamePanel.getTileSize());
        if(isEightAway(pacPos, gamePanel.getTileSize())) {
            return pacPos;
        }
        return new Point(1, 29);
    }
}
