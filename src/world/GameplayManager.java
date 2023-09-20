package world;

import main.GamePanel;

public class GameplayManager {
    private GamePanel gamePanel;
    private int dotCount = 0;
    private boolean endGame = false;


    public GameplayManager(GamePanel gamePanel) {
    }

    public boolean isEndGame() {
        // TODO update gameManager logic and return endGame if all dots are eaten or requestGameEnd has been called
        if(this.dotCount == 0) {
            this.endGame = true;
        }
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
}
