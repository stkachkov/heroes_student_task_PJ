package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {

    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;
    private static final int[] DX = {-1, -1, -1, 0, 0, 1, 1, 1};
    private static final int[] DY = {-1, 0, 1, -1, 1, -1, 0, 1};

    private static class Node implements Comparable<Node> {
        int x, y;
        double gScore;
        double fScore;
        Node parent;

        Node(int x, int y) {
            this.x = x;
            this.y = y;
            this.gScore = Double.POSITIVE_INFINITY;
            this.fScore = Double.POSITIVE_INFINITY;
        }

        @Override
        public int compareTo(Node other) {
            return Double.compare(this.fScore, other.fScore);
        }
    }

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        Set<String> obstacles = new HashSet<>();
        for (Unit unit : existingUnitList) {

            if (unit.isAlive() && unit != attackUnit && unit != targetUnit) {
                obstacles.add(unit.getxCoordinate() + "," + unit.getyCoordinate());
            }
        }

        Node startNode = new Node(attackUnit.getxCoordinate(), attackUnit.getyCoordinate());
        Node targetNode = new Node(targetUnit.getxCoordinate(), targetUnit.getyCoordinate());
        startNode.gScore = 0;
        startNode.fScore = heuristic(startNode, targetNode);

        PriorityQueue<Node> openSet = new PriorityQueue<>();
        openSet.add(startNode);
        
        Map<String, Node> allNodes = new HashMap<>();
        allNodes.put(startNode.x + "," + startNode.y, startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.x == targetNode.x && current.y == targetNode.y) {
                return reconstructPath(current);
            }

            for (int i = 0; i < 8; i++) {
                int nx = current.x + DX[i];
                int ny = current.y + DY[i];

                if (nx < 0 || nx >= WIDTH || ny < 0 || ny >= HEIGHT || obstacles.contains(nx + "," + ny)) {
                    continue;
                }

                double tentativeGScore = current.gScore + 1;

                String neighborKey = nx + "," + ny;
                Node neighbor = allNodes.computeIfAbsent(neighborKey, k -> new Node(nx, ny));

                if (tentativeGScore < neighbor.gScore) {
                    neighbor.parent = current;
                    neighbor.gScore = tentativeGScore;
                    neighbor.fScore = neighbor.gScore + heuristic(neighbor, targetNode);
                    
                    openSet.add(neighbor);
                }
            }
        }

        return new ArrayList<>();
    }

    private double heuristic(Node a, Node b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    private List<Edge> reconstructPath(Node current) {
        List<Edge> path = new LinkedList<>();
        while (current != null) {
            path.add(0, new Edge(current.x, current.y));
            current = current.parent;
        }
        return path;
    }
}