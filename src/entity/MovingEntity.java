package entity;

import world.Point;

import java.awt.*;

public interface MovingEntity extends Entity {
    String getCurrentDirection();

    default void update() {
        this.move();
        this.checkInteractions();
    }

    Rectangle getIntendedHitbox(Point IntendedPoint); // a separate hitbox exactly the size of a normal tile. Use for movement checks

    void checkInteractions();

    void move();

}
