package rs.interview.backend.service;

import rs.interview.backend.domain.Route;

import java.util.Map;

public interface CheapFlightService {

    Map<String, Double> findCheapestFlight(long sourceAirportId, long destinationAirportId);
    void updateGraph(Route route);

    void mapGraph();
}
