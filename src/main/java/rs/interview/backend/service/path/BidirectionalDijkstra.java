package rs.interview.backend.service.path;

import org.springframework.stereotype.Service;
import rs.interview.backend.service.path.graph.DirectedGraphNode;
import rs.interview.backend.service.path.graph.GraphWeightFunction;
import rs.interview.backend.service.path.heap.BinomialHeap;
import rs.interview.backend.service.path.heap.MinimumPriorityQueue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author a.ilic
 */
@Service
public class BidirectionalDijkstra implements PathFinder {

    private MinimumPriorityQueue<DirectedGraphNode> queue;

    private MinimumPriorityQueue<DirectedGraphNode> OPEN_SOURCE;
    private MinimumPriorityQueue<DirectedGraphNode> OPEN_TARGET;

    private Set<DirectedGraphNode> CLOSED_SOURCE;
    private Set<DirectedGraphNode> CLOSED_TARGET;

    private Map<DirectedGraphNode, DirectedGraphNode> PARENTSA;
    private Map<DirectedGraphNode, DirectedGraphNode> PARENTSB;

    private Map<DirectedGraphNode, Double> DISTANCEA;
    private Map<DirectedGraphNode, Double> DISTANCEB;

    private DirectedGraphNode source;
    private DirectedGraphNode target;

    //for routes price is weight function
    private final GraphWeightFunction weightFunction;

    private double bestPathLength;
    private DirectedGraphNode touchNode;


    public BidirectionalDijkstra() {
        this.weightFunction = new GraphWeightFunction();
    }

    public BidirectionalDijkstra(
            DirectedGraphNode source,
            DirectedGraphNode target,
            GraphWeightFunction weightFunction) {
        OPEN_SOURCE = getQueue() == null ? new BinomialHeap<>() : getQueue().spawn();
        OPEN_TARGET = OPEN_SOURCE.spawn();

        CLOSED_SOURCE = new HashSet<>();
        CLOSED_TARGET = new HashSet<>();

        PARENTSA = new HashMap<>();
        PARENTSB = new HashMap<>();

        DISTANCEA = new HashMap<>();
        DISTANCEB = new HashMap<>();

        this.source = source;
        this.target = target;
        this.weightFunction = weightFunction;

        this.bestPathLength = Double.POSITIVE_INFINITY;
        this.touchNode = null;
    }

    public BidirectionalDijkstra(
            GraphWeightFunction weightFunction) {
        Objects.requireNonNull(weightFunction, "The weight function is null.");
        this.weightFunction = weightFunction;
    }

    @Override
    public List<DirectedGraphNode> search(DirectedGraphNode source, DirectedGraphNode target) {
        Objects.requireNonNull(source, "The source node is null.");
        Objects.requireNonNull(target, "The target node is null.");

        return new BidirectionalDijkstra(source,
                target,
                weightFunction).search();
    }

    private List<DirectedGraphNode> search() {
        if (source.equals(target)) {
            // Bidirectional search algorithms cannont handle the case where
            // source and target nodes are same.
            List<DirectedGraphNode> path = new ArrayList<>(1);
            path.add(source);
            return path;
        }

        OPEN_SOURCE.add(source, 0.0);
        OPEN_TARGET.add(target, 0.0);

        PARENTSA.put(source, null);
        PARENTSB.put(target, null);

        DISTANCEA.put(source, 0.0);
        DISTANCEB.put(target, 0.0);

        while (!OPEN_SOURCE.isEmpty() && !OPEN_TARGET.isEmpty()) {
            double mtmp = DISTANCEA.get(OPEN_SOURCE.min()) +
                    DISTANCEB.get(OPEN_TARGET.min());

            if (mtmp >= bestPathLength) {
                return tracebackPath(touchNode, PARENTSA, PARENTSB);
            }

            if (OPEN_SOURCE.size() + CLOSED_SOURCE.size() <
                    OPEN_TARGET.size() + CLOSED_TARGET.size()) {
                expandForwardFrontier();
            } else {
                expandBackwardFrontier();
            }
        }

        return Collections.emptyList();
    }


    private void updateForwardFrontier(DirectedGraphNode node, double nodeScore) {
        if (CLOSED_TARGET.contains(node)) {
            double pathLength = DISTANCEB.get(node) + nodeScore;

            if (bestPathLength > pathLength) {
                bestPathLength = pathLength;
                touchNode = node;
            }
        }
    }

    private void updateBackwardFrontier(DirectedGraphNode node, double nodeScore) {
        if (CLOSED_SOURCE.contains(node)) {
            double pathLength = DISTANCEA.get(node) + nodeScore;

            if (bestPathLength > pathLength) {
                bestPathLength = pathLength;
                touchNode = node;
            }
        }
    }


    private void expandForwardFrontier() {
        DirectedGraphNode current = OPEN_SOURCE.extractMinimum();
        CLOSED_SOURCE.add(current);

        for (DirectedGraphNode child : current.children()) {
            if (!CLOSED_SOURCE.contains(child)) {
                double tentativeScore = DISTANCEA.get(current) +
                        weightFunction.get(current, child);

                if (!DISTANCEA.containsKey(child)) {
                    DISTANCEA.put(child, tentativeScore);
                    PARENTSA.put(child, current);
                    OPEN_SOURCE.add(child, tentativeScore);
                    updateForwardFrontier(child, tentativeScore);
                } else if (DISTANCEA.get(child) > tentativeScore) {
                    DISTANCEA.put(child, tentativeScore);
                    PARENTSA.put(child, current);
                    OPEN_SOURCE.decreasePriority(child, tentativeScore);
                    updateForwardFrontier(child, tentativeScore);
                }
            }
        }
    }

    private void expandBackwardFrontier() {
        DirectedGraphNode current = OPEN_TARGET.extractMinimum();
        CLOSED_TARGET.add(current);

        for (DirectedGraphNode parent : current.parents()) {
            if (!CLOSED_TARGET.contains(parent)) {
                double tentativeScore = DISTANCEB.get(current) +
                        weightFunction.get(parent, current);

                if (!DISTANCEB.containsKey(parent)) {
                    DISTANCEB.put(parent, tentativeScore);
                    PARENTSB.put(parent, current);
                    OPEN_TARGET.add(parent, tentativeScore);
                    updateBackwardFrontier(parent, tentativeScore);
                } else if (DISTANCEB.get(parent) > tentativeScore) {
                    DISTANCEB.put(parent, tentativeScore);
                    PARENTSB.put(parent, current);
                    OPEN_TARGET.decreasePriority(parent, tentativeScore);
                    updateBackwardFrontier(parent, tentativeScore);
                }
            }
        }
    }


    public List<DirectedGraphNode> tracebackPath(DirectedGraphNode touch,
                                                 Map<DirectedGraphNode, DirectedGraphNode> parentsA,
                                                 Map<DirectedGraphNode, DirectedGraphNode> parentsB) {
        DirectedGraphNode current = touch;
        List<DirectedGraphNode> path = new ArrayList<>();

        while (current != null) {
            path.add(current);
            current = parentsA.get(current);
        }

        Collections.reverse(path);

        if (parentsB != null) {
            current = parentsB.get(touch);

            while (current != null) {
                path.add(current);
                current = parentsB.get(current);
            }
        }

        return path;
    }

    public MinimumPriorityQueue<DirectedGraphNode> getQueue() {
        return queue;
    }

}
