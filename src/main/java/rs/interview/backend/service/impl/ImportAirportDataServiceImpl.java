package rs.interview.backend.service.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.interview.backend.domain.Airport;
import rs.interview.backend.domain.City;
import rs.interview.backend.repository.AirportRepository;
import rs.interview.backend.repository.CityRepository;
import rs.interview.backend.service.ImportAirportDataService;
import rs.interview.backend.service.mapper.AirportMapper;
import rs.interview.backend.service.mapper.CityMapper;

import javax.validation.ConstraintViolationException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ImportAirportDataServiceImpl implements ImportAirportDataService {

    private final AirportMapper airportMapper;
    private final CityMapper cityMapper;
    private final AirportRepository airportRepository;
    private final CityRepository cityRepository;
    private final Logger log = LoggerFactory.getLogger(ImportAirportDataServiceImpl.class);

    public ImportAirportDataServiceImpl(AirportMapper airportMapper, CityMapper cityMapper,
                                        AirportRepository airportRepository, CityRepository cityRepository) {
        this.airportMapper = airportMapper;
        this.cityMapper = cityMapper;
        this.airportRepository = airportRepository;
        this.cityRepository = cityRepository;
    }

    @Async
    @Override
    public void loadCitiesAirports(File file) {
        Set<City> cities = loadCities(file);
        List<Airport> airports;
        try {
            CSVParser parser = CSVParser.parse(file, Charset.defaultCharset(), CSVFormat.ORACLE);
            airports = parser.getRecords().stream().filter(this::isCsvRecordValid)
                    .map(csvRecord -> mapCsvToAirport(cities, csvRecord))
                    .filter(airport -> airport.getCity() != null)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error(String.format("Error loading airports:%s", e));
            throw new RuntimeException("Airports not loaded.");
        } catch (ConstraintViolationException e) {
            log.error(String.format("Error loading airports:%s", e));
            throw new RuntimeException("Airports are duplicates.");
        }
        airportRepository.saveAll(airports);
        log.info("Airports loaded.");
    }

    private Airport mapCsvToAirport(Set<City> cities, CSVRecord csvRecord) {
        return airportMapper.csvRecordToAirport(csvRecord, findCityForNameAndCountry(cities, csvRecord));
    }

    private City findCityForNameAndCountry(Set<City> cities, CSVRecord csvRecord) {
        return cities.stream().filter(city -> city.getName().equals(csvRecord.get(2)) &&
                city.getCountry().equals(csvRecord.get(3))).findFirst().orElse(null);
    }


    private Set<City> loadCities(File file) {
        Set<City> cities;

        try {
            CSVParser parser = CSVParser.parse(file, Charset.defaultCharset(), CSVFormat.ORACLE);
            cities = parser.getRecords().stream().filter(this::isCsvRecordValid)
                    .map(cityMapper::csvRecordToCity)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            log.error(String.format("Error loading cities:%s", e));
            throw new RuntimeException("Cities not loaded.");
        } catch (ConstraintViolationException e) {
            log.error(String.format("Error loading cities:%s", e));
            throw new RuntimeException("Cities are duplicates.");
        }
        cityRepository.saveAll(cities);
        log.info("Cities loaded.");
        return cities;
    }


    private boolean isCsvRecordValid(CSVRecord csvRecord) {
        return StringUtils.isNotEmpty(csvRecord.get(1)) &&
                StringUtils.isNotEmpty(csvRecord.get(2)) &&
                StringUtils.isNotEmpty(csvRecord.get(3));
    }

}
