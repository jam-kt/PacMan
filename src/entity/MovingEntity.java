package entity;

import world.World;

import java.awt.*;

public interface MovingEntity extends Entity {
    String getCurrentDirection();

    void update();

    Rectangle getHitbox();

    void setPosition(world.Point position);
}
