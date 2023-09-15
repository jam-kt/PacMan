package world;

import main.GamePanel;

public class GameplayManager {
    private int dotCount = 0;

    public GameplayManager(GamePanel gamePanel) {
    }

    public void increaseDotCount() {
        this.dotCount++;
    }

    public void decreaseDotCount() {
        this.dotCount--;
    }
}
