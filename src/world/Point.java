package world;

public final class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public boolean equals(Object other) {
            return other instanceof Point &&
                    ((Point)other).x == this.x &&
                    ((Point)other).y == this.y;
        }

    public int hashCode() {
        int result = 17;
        result = result * 31 + x;
        result = result * 31 + y;
        return result;
    }

    public static boolean adjacent(Point p1, Point p2) {
        return (p1.x == p2.x && Math.abs(p1.y - p2.y) == 1) || (p1.y == p2.y && Math.abs(p1.x - p2.x) == 1);
    }

    public static Point scaleDown(Point point, int scale) {
        return new Point(point.x / scale, point.y / scale);
    }

    public static Point scaleUp(Point point, int scale) {
        return new Point(point.x / scale, point.y / scale);
    }

    // will only be used in context of a situation where a pathing strategy returns a new point, aka not the same two
    // points. Also will only be used in a context where only X or only Y will change, never both at once.
    // not very robust code but still works if used for its intended purposes
    public static String findDirectionFrom(Point start, Point end) {
        int deltaX = end.x - start.x;
        int deltaY = end.y - start.y;
        if(deltaX > 0) {
            return "right";
        }
        else if(deltaX < 0) {
            return "left";
        }
        else if(deltaY > 0) { // remember 0,0 is the top left corner
            return "down";
        }
        else {
            return "up";
        }
    }

}
