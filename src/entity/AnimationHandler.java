package entity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class AnimationHandler { // not the most strict class regarding OOP adherence, but easier since game only has few animations
    private final Entity entity;
    private final int interval; // how many frames each sprite will be displayed for 60/interval = animation frame rate
    private int framesPassed; // for internal logic
    private int currentFrame = 0; // ^
    private BufferedImage lastFrame; // ^
    private final ArrayList<BufferedImage> up = new ArrayList<>(); // a list to store the frames for each direction
    private final ArrayList<BufferedImage> down = new ArrayList<>();
    private final ArrayList<BufferedImage> left = new ArrayList<>();
    private final ArrayList<BufferedImage> right = new ArrayList<>();
    private ArrayList<BufferedImage> directionInUse; // stores the list that corresponds to the entities current direction

    public AnimationHandler(Entity entity, int interval) {
        this.entity = entity;
        this.interval = interval; // how many frames each sprite will be displayed for 60/interval = animation frame rate
        this.framesPassed = interval;

        this.getImages();
    }

    private void getImages() { // get and add appropriate images to the four lists based on class type
        try {
            if (entity.getClass().equals(PacMan.class)) {
                this.up.add(ImageIO.read(getClass().getResourceAsStream("/spriteFrames/tile002.png")));
                this.up.add(ImageIO.read(getClass().getResourceAsStream("/spriteFrames/tile029.png")));
                this.up.add(ImageIO.read(getClass().getResourceAsStream("/spriteFrames/tile028.png")));
                this.up.add(ImageIO.read(getClass().getResourceAsStream("/spriteFrames/tile029.png")));
                this.left.add(ImageIO.read(getClass().getResourceAsStream("/spriteFrames/tile002.png")));
                this.left.add(ImageIO.read(getClass().getResourceAsStream("/spriteFrames/tile015.png")));
                this.left.add(ImageIO.read(getClass().getResourceAsStream("/spriteFrames/tile014.png")));
                this.left.add(ImageIO.read(getClass().getResourceAsStream("/spriteFrames/tile015.png")));
                this.right.add(ImageIO.read(getClass().getResourceAsStream("/spriteFrames/tile002.png")));
                this.right.add(ImageIO.read(getClass().getResourceAsStream("/spriteFrames/tile001.png")));
                this.right.add(ImageIO.read(getClass().getResourceAsStream("/spriteFrames/tile000.png")));
                this.right.add(ImageIO.read(getClass().getResourceAsStream("/spriteFrames/tile001.png")));
                this.down.add(ImageIO.read(getClass().getResourceAsStream("/spriteFrames/tile002.png")));
                this.down.add(ImageIO.read(getClass().getResourceAsStream("/spriteFrames/tile043.png")));
                this.down.add(ImageIO.read(getClass().getResourceAsStream("/spriteFrames/tile042.png")));
                this.down.add(ImageIO.read(getClass().getResourceAsStream("/spriteFrames/tile043.png")));
            }
        }
        catch(IOException e) {e.printStackTrace();}
    }

    public BufferedImage getNextFrame() { // called by the entity every time it gets drawn
        framesPassed++;
        if (this.framesPassed >= this.interval) {
            this.framesPassed = 0;

            switch (this.entity.getCurrentDirection()) {
                case "up" -> this.directionInUse = up;
                case "down" -> this.directionInUse = down;
                case "left" -> this.directionInUse = left;
                case "right" -> this.directionInUse = right;
            }
            if (currentFrame >= directionInUse.size() - 1) {
                currentFrame = 0;
                this.lastFrame = directionInUse.get(directionInUse.size() - 1);
                return lastFrame;
            }
            else {
                BufferedImage temp = directionInUse.get(currentFrame);
                currentFrame++;
                lastFrame = temp;
                return temp;
            }
        }
        else {return lastFrame;}
    }
}
