package rs.interview.backend.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import rs.interview.backend.security.AuthoritiesConstants;
import rs.interview.backend.service.ImportAirportDataService;
import rs.interview.backend.service.StorageService;

import java.io.File;

@RestController
@RequestMapping("/api/airports")
public class AirportController {


    private final ImportAirportDataService importAirportDataService;
    private final StorageService storageService;

    public AirportController(ImportAirportDataService importAirportDataService, StorageService storageService) {
        this.importAirportDataService = importAirportDataService;
        this.storageService = storageService;
    }

    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @PostMapping(value = "/import")
    public ResponseEntity<Void> importAirportsCsvFile(@RequestParam("file") MultipartFile multipartFile) {
        File file = storageService.saveFile(multipartFile);
        importAirportDataService.loadCitiesAirports(file);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
