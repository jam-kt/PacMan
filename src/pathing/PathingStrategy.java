package pathing;

import world.Point;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface PathingStrategy
{
   /**
    * Returns a prefix of a path from the start point to a point within reach
    * of the end point.  This path is only valid ("clear") when returned, but
    * may be invalidated by movement of other entities.
    * The prefix includes neither the start point nor the end point.
    * START AND END POINTS MUST BE GIVEN AS TILE MAP COORDINATES, NOT PIXELPOINT. SCALE DOWN POINTS WHEN
    * PASSING POSITIONS AS ARGUMENTS. Although movement happens at the pixel level, pathing is at tile level
    */
   List<Point> computePath(Point start, Point end,
                           Predicate<Point> canPassThrough,
                           BiPredicate<Point, Point> withinReach,
                           Function<Point, Stream<Point>> potentialNeighbors, int tileSize);

   Function<Point, Stream<Point>> CARDINAL_NEIGHBORS =
      point ->
         Stream.<Point>builder()
            .add(new Point(point.x, point.y - 1))
            .add(new Point(point.x, point.y + 1))
            .add(new Point(point.x - 1, point.y))
            .add(new Point(point.x + 1, point.y))
            .build();

   Function<Point, Stream<Point>> DIAGONAL_CARDINAL_NEIGHBORS =
        point ->
                Stream.<Point>builder()
                        .add(new Point(point.x - 1, point.y - 1))
                        .add(new Point(point.x + 1, point.y + 1))
                        .add(new Point(point.x - 1, point.y + 1))
                        .add(new Point(point.x + 1, point.y - 1))
                        .add(new Point(point.x, point.y - 1))
                        .add(new Point(point.x, point.y + 1))
                        .add(new Point(point.x - 1, point.y))
                        .add(new Point(point.x + 1, point.y))
                        .build();

   Function<Point, Stream<Point>> CARDINAL_NEIGHBORS_UP =
           point ->
                   Stream.<Point>builder()
                           .add(new Point(point.x, point.y - 1))
                           .add(new Point(point.x - 1, point.y))
                           .add(new Point(point.x + 1, point.y))
                           .build();

   Function<Point, Stream<Point>> CARDINAL_NEIGHBORS_DOWN =
           point ->
                   Stream.<Point>builder()
                           .add(new Point(point.x, point.y + 1))
                           .add(new Point(point.x - 1, point.y))
                           .add(new Point(point.x + 1, point.y))
                           .build();

   Function<Point, Stream<Point>> CARDINAL_NEIGHBORS_LEFT =
           point ->
                   Stream.<Point>builder()
                           .add(new Point(point.x, point.y - 1))
                           .add(new Point(point.x, point.y + 1))
                           .add(new Point(point.x - 1, point.y))
                           .build();

   Function<Point, Stream<Point>> CARDINAL_NEIGHBORS_RIGHT =
           point ->
                   Stream.<Point>builder()
                           .add(new Point(point.x, point.y - 1))
                           .add(new Point(point.x, point.y + 1))
                           .add(new Point(point.x + 1, point.y))
                           .build();
}

