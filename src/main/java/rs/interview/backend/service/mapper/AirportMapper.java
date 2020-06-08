package rs.interview.backend.service.mapper;

import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import rs.interview.backend.domain.Airport;
import rs.interview.backend.domain.City;

import java.math.BigDecimal;

@Service
public class AirportMapper {


    public Airport csvRecordToAirport(CSVRecord csvRecord, City city) {
        Airport airport = new Airport();
        airport.setExternalId(loadExternalId(csvRecord));
        airport.setName(csvRecord.get(1));
        airport.setCity(city);
        airport.setIata(csvRecord.get(4));
        airport.setIcao(csvRecord.get(5));
        airport.setDst(csvRecord.get(10));
        airport.setTimezoneOlson(csvRecord.get(11));
        airport.setLatitude(loadLatitude(csvRecord));
        airport.setLongitude(loadLongitude(csvRecord));
        airport.setTimezoneUtc(loadTimezoneUtc(csvRecord));
        airport.setAltitude(loadAltitude(csvRecord));
        return airport;
    }


    private BigDecimal loadLatitude(CSVRecord csvRecord) {
        return csvRecord.get(6) != null ? new BigDecimal(csvRecord.get(6)) : null;
    }

    private BigDecimal loadLongitude(CSVRecord csvRecord) {
        return csvRecord.get(7) != null ? new BigDecimal(csvRecord.get(7)) : null;
    }

    private Float loadTimezoneUtc(CSVRecord csvRecord) {
        return csvRecord.get(9) != null ? Float.valueOf(csvRecord.get(9)) : null;
    }

    private Long loadAltitude(CSVRecord csvRecord) {
        return csvRecord.get(8) != null ? Long.valueOf(csvRecord.get(8)) : null;
    }

    private Long loadExternalId(CSVRecord csvRecord) {
        return csvRecord.get(0) != null ? Long.valueOf(csvRecord.get(0)) : null;
    }
}
