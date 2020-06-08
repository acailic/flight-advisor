package rs.interview.backend.service.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.interview.backend.domain.Route;
import rs.interview.backend.repository.RouteRepository;
import rs.interview.backend.service.CheapFlightService;
import rs.interview.backend.service.ImportRouteDataService;
import rs.interview.backend.service.mapper.RouteMapper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ImportRouteDataServiceImpl implements ImportRouteDataService {
    private final RouteMapper routeMapper;
    private final RouteRepository routeRepository;
    private final CheapFlightService cheapFlightService;
    private final Logger log = LoggerFactory.getLogger(ImportRouteDataServiceImpl.class);

    public ImportRouteDataServiceImpl(RouteMapper routeMapper, RouteRepository routeRepository,
                                      CheapFlightService cheapFlightService) {
        this.routeMapper = routeMapper;
        this.routeRepository = routeRepository;
        this.cheapFlightService = cheapFlightService;
    }

    @Override
    @Async
    public void loadRoutes(File file) {
        List<Route> routes;

        try {
            CSVParser parser = CSVParser.parse(file, Charset.defaultCharset(), CSVFormat.ORACLE);
            routes = parser.getRecords().stream()
                    .map(routeMapper::csvRecordToRoute).filter(this::isValidNotBlank).collect(Collectors.toList());
        } catch (IOException e) {
            log.error(String.format("Error loading routes:%s", e));
            throw new RuntimeException("Routes not loaded.");
        }
        routeRepository.saveAll(routes);
        routes.forEach(cheapFlightService::updateGraph);
        log.info("Routes loaded.");
    }

    private boolean isValidNotBlank(Route route) {
        return StringUtils.isNotBlank(route.getAirlineId())
                && route.getStops() != null && route.getPrice() != null;
    }
}
