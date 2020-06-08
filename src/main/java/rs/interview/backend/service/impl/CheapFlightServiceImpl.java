package rs.interview.backend.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.interview.backend.domain.Airport;
import rs.interview.backend.domain.Route;
import rs.interview.backend.repository.AirportRepository;
import rs.interview.backend.repository.RouteRepository;
import rs.interview.backend.service.CheapFlightService;
import rs.interview.backend.service.path.BidirectionalDijkstra;
import rs.interview.backend.service.path.graph.DirectedGraphNode;
import rs.interview.backend.service.path.graph.GraphWeightFunction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class CheapFlightServiceImpl implements CheapFlightService {

    private Set<DirectedGraphNode> graph = new HashSet<>();
    private final GraphWeightFunction graphWeightFunction;
    private final RouteRepository routeRepository;
    private final AirportRepository airportRepository;

    public CheapFlightServiceImpl(GraphWeightFunction graphWeightFunction, RouteRepository routeRepository,
                                  AirportRepository airportRepository) {
        this.graphWeightFunction = graphWeightFunction;
        this.routeRepository = routeRepository;
        this.airportRepository = airportRepository;
    }

    @Override
    public Map<String, Double> findCheapestFlight(long sourceAirportId, long destinationAirportId) {
        /*    TODO: Serialize graph
         *     Graphs is in memory.
         */
        if (graph.isEmpty()) {
            createGraphSet();
        }

        DirectedGraphNode sourceAirport =
                graph.stream().filter(directedGraphNode -> directedGraphNode.getId().equals(sourceAirportId)).findFirst().orElse(null);
        DirectedGraphNode destinationAirport =
                graph.stream().filter(directedGraphNode -> directedGraphNode.getId().equals(destinationAirportId)).findFirst().orElse(null);
        List<DirectedGraphNode> directedGraphNodeList =
                new BidirectionalDijkstra(graphWeightFunction).
                        search(sourceAirport
                                , destinationAirport);
        return getPathResults(directedGraphNodeList, graphWeightFunction);
    }

    @Override
    public void updateGraph(Route route) {
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

    public Map<String, Double> getPathResults(List<DirectedGraphNode> path,
                                              GraphWeightFunction weightFunction) {
        double length = 0.0;
        Map<String, Double> result = new LinkedHashMap<>();
        for (int i = 0; i < path.size() - 1; ++i) {
            if (!path.get(i).hasChild(path.get(i + 1))) {
                continue;
            }
            result.put(getAirportNameCityCountry(path.get(i).getId()) + " ------>"
                    + getAirportNameCityCountry(path.get(i + 1).getId()), weightFunction.get(path.get(i),
                    path.get(i + 1)));
            length += weightFunction.get(path.get(i), path.get(i + 1));
        }
        result.put("Total",
                new BigDecimal(length).setScale(2, RoundingMode.HALF_UP).doubleValue());
        return result;
    }


    private String getAirportNameCityCountry(Long externalId) {
        Airport airport = airportRepository.findAirportByExternalId(externalId);
        return airport.getName() + "/" + airport.getCity().getName() + "/" + airport.getCity().getCountry();
    }
    /*
          Graph is not serialized. Everything in memory.
         TODO: Serialize graph into db.
          mongo or neo4j
          */

    @Override
    public void mapGraph() {
        createGraphSet();
    }

    private void createGraphSet() {
        routeRepository.findAll().forEach(this::updateGraph);
    }
}
