package entity;

import world.Point;

import java.awt.*;

public interface Entity {


    Point getPosition();


    void draw(Graphics2D graphics2D, int width, int height);
}
