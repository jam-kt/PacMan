package entity;

import world.Point;

import java.awt.*;

public interface MovingEntity extends Entity {
    String getCurrentDirection();

    void update();

    Rectangle getCurrentHitbox(); // use as a entity-entity collision hitbox. Can be smaller than tile size

    Rectangle getIntendedHitbox(Point IntendedPoint); // a separate hitbox exactly the size of a normal tile. Use for movement checks

    void checkInteractions();

    void setPosition(world.Point position);

    void move();

}
