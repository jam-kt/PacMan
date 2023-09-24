package world;

import entity.*;

public class GameplayManager {
    private World world;
    private int dotCount = 0;
    private boolean endGame = false;
    private int poweredState = 0;
    private int score = 0;


    public GameplayManager(World world) {
        this.world = world;
    }

    public boolean isEndGame() {
        return this.endGame;
    }

    public void increaseDotCount() {
        this.dotCount++;
    }

    public void decreaseDotCount() {
        this.dotCount--;
    }

    public void requestGameEnd() {
        this.endGame = true;
    }

    public void update() {
        if (this.dotCount == 0) {
            this.endGame = true;
        }
        if (this.poweredState > 0) {
            poweredState --;
            if(this.poweredState == 0) {
                deactivatePoweredState();
            }
        }
    }

    public void initiatePoweredState() {
        if(this.poweredState > 0) {
            this.poweredState += 300;
        }
        else {
            this.poweredState = 300;
            for (Entity entity : this.world.getMovingEntities()) {
                if (entity instanceof entity.Ghost) {
                    Point tempPosition = entity.getPosition();
                    String tempDirection = ((Ghost) entity).getCurrentDirection();
                    this.world.removeEntity(entity);
                    this.world.addEntity(new ScaredGhost(world.gamePanel, world, tempPosition, 7, entity.getClass().getName(), tempDirection));
                }
            }
        }
    }

    private void deactivatePoweredState() {
        for(Entity entity : this.world.getMovingEntities()) {
            if(entity instanceof entity.ScaredGhost) {
                switch (((ScaredGhost) entity).myType) {
                    case "entity.Blinky" -> this.world.addEntity(new Blinky(world.gamePanel, world, entity.getPosition(), 7, ((ScaredGhost) entity).getCurrentDirection()));
                    case "entity.Clyde" -> this.world.addEntity(new Clyde(world.gamePanel, world, entity.getPosition(), 7, ((ScaredGhost) entity).getCurrentDirection()));
                    case "entity.Inky" -> this.world.addEntity(new Inky(world.gamePanel, world, entity.getPosition(), 7, ((ScaredGhost) entity).getCurrentDirection()));
                    case "entity.Pinky" -> this.world.addEntity(new Pinky(world.gamePanel, world, entity.getPosition(), 7, ((ScaredGhost) entity).getCurrentDirection()));
                }
                this.world.removeEntity(entity);
            }
        }
    }
}
