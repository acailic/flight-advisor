package rs.interview.backend.service.path.graph;

import java.math.BigDecimal;

public class RouteMock {

    private String sourceAirport;
    private Long sourceAirportId;
    private String destinationAirport;
    private Long destinationAirportId;
    private BigDecimal price;

    public String getSourceAirport() {
        return sourceAirport;
    }

    public void setSourceAirport(String sourceAirport) {
        this.sourceAirport = sourceAirport;
    }

    public Long getSourceAirportId() {
        return sourceAirportId;
    }

    public void setSourceAirportId(Long sourceAirportId) {
        this.sourceAirportId = sourceAirportId;
    }

    public String getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(String destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    public Long getDestinationAirportId() {
        return destinationAirportId;
    }

    public void setDestinationAirportId(Long destinationAirportId) {
        this.destinationAirportId = destinationAirportId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public RouteMock(Long sourceAirportId, Long destinationAirportId, BigDecimal price) {
        this.sourceAirportId = sourceAirportId;
        this.destinationAirportId = destinationAirportId;
        this.price = price;
    }

    public static RouteBuilder builder() {
        return new RouteBuilder();
    }


    public static class RouteBuilder {
        RouteBuilder() {
        }

        Long sourceAirportId;
        Long destinationAirportId;
        BigDecimal price;

        public RouteBuilder sourceAirportId(Long sourceAirportId) {
            this.sourceAirportId = sourceAirportId;
            return this;
        }

        public RouteBuilder destinationAirportId(Long destinationAirportId) {
            this.destinationAirportId = destinationAirportId;
            return this;
        }

        public RouteBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }


        public RouteMock build() {
            return new RouteMock(sourceAirportId, destinationAirportId, price);
        }
    }

}
