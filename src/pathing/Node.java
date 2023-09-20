package pathing;

import world.Point;

public class Node {
    public Point point;
    public Node previous;
    public int g; // known distance from the start node
    public int h; // heuristic
    public int f; // g + h

    public Node(Point point, Node previous, int g, int h) {
        this.point = point;
        this.previous = previous;
        this.g = g;
        this.h = h;
        this.f = this.g + this.h;
    }

    //for the creation of our root node (starting point)
    public Node(Point point, int h) {
        this.point = point;
        this.previous = null;
        this.g = 0;
        this.h = h;
        this.f = this.g + this.h;
    }

    public int getF() {return this.f;}
}
