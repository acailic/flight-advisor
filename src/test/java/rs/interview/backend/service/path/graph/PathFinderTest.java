package rs.interview.backend.service.path.graph;

import org.junit.jupiter.api.Test;
import rs.interview.backend.service.path.BidirectionalDijkstra;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PathFinderTest {


    /**
     * Ids of airports:
     * BG - 1
     * FR - 2
     * DB - 3
     * MAL- 4
     * LIS -5
     * <p>
     * <p>
     * <p>
     * Source: Belgrade (1)
     * Destination: Maldives (4)
     * RouteMock 1: BG -> DUB -> MAL = 105 + 10 = 115
     * RouteMock 2: BG -> FRA -> DUB -> MAL = 40 + 90 + 20 = 150
     * RouteMock 3: BG -> LIS -> MAL = 130 + 30 = 160
     * RouteMock 4: BG -> MAL = 200

     * Assert: Route 1 -> 115
     **/




    public static final class GraphData {
        public Set<DirectedGraphNode> graph;
        public GraphWeightFunction weightFunction;
    }

    @Test
    public void test() {

        GraphData data = getRandomGraphData();

        DirectedGraphNode sourceBG = getRandomGraphData().graph.stream()
                .filter(directedGraphNode -> directedGraphNode.getId().equals(1L)).findFirst().orElse(null);
        DirectedGraphNode destinationMAL = getRandomGraphData().graph.stream()
                .filter(directedGraphNode -> directedGraphNode.getId().equals(4L)).findFirst().orElse(null);

        long startTime = System.currentTimeMillis();
        List<DirectedGraphNode> path1
                = new BidirectionalDijkstra(data.weightFunction)
                .search(sourceBG, destinationMAL);
        long endTime = System.currentTimeMillis();
        System.out.println("DijkstraPathFinder in " + (endTime - startTime) +
                " milliseconds.");
        System.out.println("Path length: " +
                getPathLength(path1, data.weightFunction));
        assertEquals(115D, getPathLength(path1, data.weightFunction));

    }


    public GraphData getRandomGraphData() {
        Set<DirectedGraphNode> graph = new HashSet<>();
        GraphWeightFunction weightFunction =
                new GraphWeightFunction();
        mockRoutes().forEach(routeMock -> updateGraph(routeMock, weightFunction, graph));

        GraphData ret = new GraphData();
        ret.graph = graph;
        ret.weightFunction = weightFunction;
        return ret;
    }


    private static double
    getPathLength(List<DirectedGraphNode> path,
                  GraphWeightFunction weightFunction) {
        double length = 0.0;

        for (int i = 0; i < path.size() - 1; ++i) {
            if (!path.get(i).hasChild(path.get(i + 1))) {
                return Double.NaN;
            }

            length += weightFunction.get(path.get(i), path.get(i + 1));
        }

        return length;
    }


    private List<RouteMock> mockRoutes() {
        List<RouteMock> routeList = new ArrayList<>();
        RouteMock beogradFrankfurt = RouteMock.builder().sourceAirportId(1L).destinationAirportId(2L)
                .price(BigDecimal.valueOf(40)).build();
        routeList.add(beogradFrankfurt);
        RouteMock beogradDubai = RouteMock.builder().sourceAirportId(1L).destinationAirportId(3L)
                .price(BigDecimal.valueOf(105)).build();
        routeList.add(beogradDubai);
        RouteMock beogradLisabon = RouteMock.builder().sourceAirportId(1L).destinationAirportId(5L)
                .price(BigDecimal.valueOf(130)).build();
        routeList.add(beogradLisabon);
        RouteMock beogradMaldivi = RouteMock.builder().sourceAirportId(1L).destinationAirportId(4L)
                .price(BigDecimal.valueOf(200)).build();
        routeList.add(beogradMaldivi);
        RouteMock frankfurtDubai =
                RouteMock.builder().sourceAirportId(2L).destinationAirportId(3L)
                        .price(BigDecimal.valueOf(90)).build();
        routeList.add(frankfurtDubai);
        RouteMock dubaiMaldivi =
                RouteMock.builder().sourceAirportId(3L).destinationAirportId(4L)
                        .price(BigDecimal.valueOf(10)).build();
        routeList.add(dubaiMaldivi);

        RouteMock lisabonMaldivi =
                RouteMock.builder().sourceAirportId(5L).destinationAirportId(4L)
                        .price(BigDecimal.valueOf(20)).build();
        routeList.add(lisabonMaldivi);

        return routeList;
    }

    public void updateGraph(RouteMock route, GraphWeightFunction graphWeightFunction, Set<DirectedGraphNode> graph) {
        if (route.getSourceAirportId() != null && route.getDestinationAirportId() != null) {
            DirectedGraphNode sourceNode = graph.stream().filter(directedGraphNode -> directedGraphNode.getId().
                    equals(route.getSourceAirportId())).findFirst().orElse(new DirectedGraphNode(route.getSourceAirportId()));
            DirectedGraphNode destinationNode = graph.stream().filter(directedGraphNode -> directedGraphNode.getId().
                    equals(route.getDestinationAirportId())).findFirst().orElse(new DirectedGraphNode(route.getDestinationAirportId()));
            connect(sourceNode, destinationNode, graphWeightFunction, route.getPrice());
            graph.add(sourceNode);
            graph.add(destinationNode);
        }
    }

    private void connect(DirectedGraphNode source,
                         DirectedGraphNode destination,
                         GraphWeightFunction graphWeightFunction,
                         BigDecimal weight) {
        source.addChild(destination);
        graphWeightFunction.put(source, destination, weight);
    }
}
