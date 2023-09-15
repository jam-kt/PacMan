package main;

public class GameThread implements Runnable {
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS;

    public GameThread(GamePanel gamePanel, int FPS) {
        this.gamePanel = gamePanel;
        this.gameThread = new Thread(this);
        this.FPS = FPS;
    }

    public void start() {
        this.gameThread.start();
    }

    @Override
    public void run() { // drives the update and render cycle for the gamePanel at 60 FPS
        double interval = 1000000000/FPS;
        double delta = 0;
        long oldTime = System.nanoTime();
        long currentTime;

        while(this.gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - oldTime) / interval;
            oldTime = currentTime;

            if(delta >= 1) {
                gamePanel.update();
                gamePanel.repaint();
                delta --;
            }
        }
    }
}
