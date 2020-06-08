package rs.interview.backend.service.mapper;

import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import rs.interview.backend.domain.Route;

import java.math.BigDecimal;

@Service
public class RouteMapper {

    public Route csvRecordToRoute(CSVRecord csvRecord) {
        Route route = new Route();
        route.setSourceAirportId(loadSourceAirportId(csvRecord));
        route.setSourceAirportCode(csvRecord.get(2));
        route.setDestinationAirportId(loadDestinationAirportId(csvRecord));
        route.setDestinationAirportCode(csvRecord.get(4));
        route.setAirlineId(csvRecord.get(1));
        route.setAirline(csvRecord.get(0));
        route.setCodeShare(csvRecord.get(6));
        route.setStops(loadNumberOfStops(csvRecord));
        route.setEquipment(csvRecord.get(8));
        route.setPrice(loadPrice(csvRecord));
        return route;
    }


    private Integer loadNumberOfStops(CSVRecord csvRecord) {
        return StringUtils.isEmpty(csvRecord.get(7)) ? null : Integer.valueOf(csvRecord.get(7));
    }

    private Long loadSourceAirportId(CSVRecord csvRecord) {
        return csvRecord.get(3) != null ? Long.valueOf(csvRecord.get(3)) : null;
    }

    private Long loadDestinationAirportId(CSVRecord csvRecord) {
        return csvRecord.get(5) != null ? Long.valueOf(csvRecord.get(5)) : null;
    }

    private BigDecimal loadPrice(CSVRecord csvRecord) {
        if (csvRecord.get(9) != null) {
            return new BigDecimal(csvRecord.get(9));
        } else {
            //log.warning(String.format("Price missing for route with source/destination [%s/%s], setting max price.", csvRecord.get(2), csvRecord.get(4)));
            return BigDecimal.valueOf(Double.MAX_VALUE);
        }
    }
}
