package pathing;

import world.Point;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy { // CALCULATES IN INCREMENTS OF 1 FOR X & Y OF POINTS

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors, int tileSize)
    {
        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparing(Node::getF));
        // openListHash is used for searching if a point is already on openList, and for altering values in the priorityQueue
        HashMap<Point, Node> openListHash = new HashMap<>();
        HashMap<Point, Integer> closedList = new HashMap<>();
        List<Point> outlist = new ArrayList<>();
        Node currentNode = new Node(start, heuristicDistance(start, end));
        boolean firstNode = true; // this is needed because the first direction taken must adhere to the no 180 rule for ghosts
        List<Point> neighbors;

        while(!withinReach.test(currentNode.point, end) || firstNode) {
            closedList.put(currentNode.point, currentNode.f);
            if(firstNode) { // the first direction taken will adhere to the no 180 rule
                neighbors = potentialNeighbors.apply(currentNode.point)
                        .filter(canPassThrough.and(p -> !closedList.containsKey(p))).toList();
                firstNode = false;
            }
            // all other points chosen on the path will not be restricted to the original direction, and will not allow
            // 180 movement either because of the closedList
            else {
                neighbors = PathingStrategy.CARDINAL_NEIGHBORS.apply(currentNode.point)
                        .filter(canPassThrough.and(p -> !closedList.containsKey(p))).toList();
            }

            for(Point point : neighbors) {
                Node newNode = new Node(point, currentNode, currentNode.g + 1, heuristicDistance(point, end));
                if(openListHash.containsKey(point)) { // this section handles cases where the neighbor has already been put into openlist
                    if(newNode.g <= openListHash.get(point).g) {
                        openList.remove(openListHash.get(point)); // bad for performance, maybe make new heap instead
                        openListHash.replace(point, newNode);
                        openList.add(newNode);
                    }
                }
                else {
                    openList.add(newNode);
                    openListHash.put(point, newNode);
                }
            }

            if(!openList.isEmpty()) {
                currentNode = openList.poll();  //takes node with best F value, loop
            }
            else {return outlist;}
        }

        while(currentNode.previous != null) { // while not the start node in the path, add node to path
            outlist.add(0, currentNode.point);
            currentNode = currentNode.previous;
        }
        return outlist;
    }

    public int heuristicDistance(Point start, Point end) {
        return(Math.abs(start.x - end.x) + Math.abs(start.y - end.y));
    }
}
