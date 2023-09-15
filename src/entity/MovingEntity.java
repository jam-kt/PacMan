package entity;

import world.Point;

import java.awt.*;

public interface MovingEntity extends Entity {
    String getCurrentDirection();

    void update();

    Rectangle getCurrentHitbox();

    Rectangle getIntendedHitbox(Point IntendedPoint);

    void checkInteractions();

    void setPosition(world.Point position);

    void move();

}
