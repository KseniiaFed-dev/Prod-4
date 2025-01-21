package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.EdgeDistance;
import com.battle.heroes.army.programs.UnitTargetPathFinder;
import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {

    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        if (attackUnit == null || targetUnit == null || existingUnitList == null) {
            return new ArrayList<>();
        }

        int startX = -1, startY = -1;
        int endX = -1, endY = -1;

        // Find attackUnit and targetUnit coordinates
        for (Unit unit : existingUnitList) {
            if (attackUnit.equals(unit)) {
                startX = unit.hashCode() % WIDTH;
                startY = unit.hashCode() % HEIGHT;

                if (startX < 0) startX = -startX;
                if (startY < 0) startY = -startY;

                if (startX >= WIDTH) startX %= WIDTH;
                if (startY >= HEIGHT) startY %= HEIGHT;

            } else if (targetUnit.equals(unit)) {
                endX = unit.hashCode() % WIDTH;
                endY = unit.hashCode() % HEIGHT;

                if (endX < 0) endX = -endX;
                if (endY < 0) endY = -endY;


                if (endX >= WIDTH) endX %= WIDTH;
                if (endY >= HEIGHT) endY %= HEIGHT;


            }
            if(startX != -1 && endX != -1)
                break;

        }

        if (startX == -1 || endX == -1) {
            return new ArrayList<>(); // Unit coordinates not found, can't create a path
        }

        if (startX < 0 || startX >= WIDTH || startY < 0 || startY >= HEIGHT ||
                endX < 0 || endX >= WIDTH || endY < 0 || endY >= HEIGHT) {
            return new ArrayList<>();
        }


        PriorityQueue<EdgeDistance> queue = new PriorityQueue<>(Comparator.comparingInt(EdgeDistance::getDistance));
        Map<String, EdgeDistance> visited = new HashMap<>();
        Map<String, Edge> previous = new HashMap<>();

        EdgeDistance start = new EdgeDistance(startX, startY, 0);
        queue.add(start);
        visited.put(getKey(startX, startY), start);

        while (!queue.isEmpty()) {
            EdgeDistance current = queue.poll();
            int x = current.getX();
            int y = current.getY();

            if (x == endX && y == endY) {
                return buildPath(previous, startX, startY, endX, endY);
            }

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (Math.abs(dx) + Math.abs(dy) != 1) {
                        continue;
                    }

                    int nextX = x + dx;
                    int nextY = y + dy;

                    if (nextX >= 0 && nextX < WIDTH && nextY >= 0 && nextY < HEIGHT) {
                        boolean isOccupied = false;
                        for(Unit unit: existingUnitList){
                            int unitX = unit.hashCode() % WIDTH;
                            int unitY = unit.hashCode() % HEIGHT;

                            if (unitX < 0) unitX = -unitX;
                            if (unitY < 0) unitY = -unitY;


                            if (unitX >= WIDTH) unitX %= WIDTH;
                            if (unitY >= HEIGHT) unitY %= HEIGHT;


                            if(unitX == nextX && unitY == nextY){
                                isOccupied = true;
                                break;
                            }
                        }

                        if(!isOccupied){
                            String nextKey = getKey(nextX, nextY);
                            int newDistance = current.getDistance() + 1;
                            if (!visited.containsKey(nextKey) || newDistance < visited.get(nextKey).getDistance()) {
                                EdgeDistance nextEdge = new EdgeDistance(nextX, nextY, newDistance);
                                queue.add(nextEdge);
                                visited.put(nextKey, nextEdge);
                                previous.put(nextKey, new Edge(x,y));
                            }
                        }
                    }
                }
            }
        }
        return new ArrayList<>(); // No path found
    }

    private String getKey(int x, int y){
        return x + "-" + y;
    }

    private List<Edge> buildPath(Map<String, Edge> previous, int startX, int startY, int endX, int endY) {
        List<Edge> path = new ArrayList<>();
        int currentX = endX;
        int currentY = endY;

        while (currentX != startX || currentY != startY) {
            path.add(0, new Edge(currentX, currentY));
            String currentKey = getKey(currentX, currentY);
            Edge prev = previous.get(currentKey);
            if(prev == null)
                return new ArrayList<>();
            currentX = prev.getX();
            currentY = prev.getY();
        }
        path.add(0, new Edge(startX, startY));

        return path;
    }
}



