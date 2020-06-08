package rs.interview.backend.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import rs.interview.backend.security.AuthoritiesConstants;
import rs.interview.backend.service.CheapFlightService;
import rs.interview.backend.service.ImportRouteDataService;
import rs.interview.backend.service.StorageService;

import java.io.File;
import java.util.Map;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final ImportRouteDataService importRouteDataService;
    private final StorageService storageService;
    private final CheapFlightService cheapFlightService;

    public RouteController(ImportRouteDataService importRouteDataService, StorageService storageService,
                           CheapFlightService cheapFlightService) {
        this.importRouteDataService = importRouteDataService;
        this.storageService = storageService;
        this.cheapFlightService = cheapFlightService;
    }

    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @PostMapping(value = "/import")
    public ResponseEntity<Void> importRoutesCsv(@RequestParam("file") MultipartFile multipartFile) {
        File file = storageService.saveFile(multipartFile);
        importRouteDataService.loadRoutes(file);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    @GetMapping("/cheapest-price-route")
    public ResponseEntity<Map<String, Double>> getLowestPricePath(@RequestParam("sourceAirportId") Long sourceAirportId,
                                                                  @RequestParam("destinationAirportId") Long destinationAirportId) {
        Map<String, Double> flightRoutes = cheapFlightService.findCheapestFlight(sourceAirportId, destinationAirportId);
        return ResponseEntity.ok().body(flightRoutes);
    }


    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.USER + "\")")
    @GetMapping("/map-graph")
    public ResponseEntity<Void> mapGraph() {
        cheapFlightService.mapGraph();
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
