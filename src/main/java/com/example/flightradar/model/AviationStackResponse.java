package com.example.flightradar.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AviationStackResponse {
    private List<FlightInfo> data;

    @Data
    public static class FlightInfo {
        @JsonProperty("flight_date")
        private String flightDate;

        @JsonProperty("flight_status")
        private String flightStatus;

        private Departure departure;
        private Arrival arrival;
        private Airline airline;
        private Flight flight;
        private Aircraft aircraft;
        private Live live;
    }

    @Data
    public static class Departure {
        private String airport;
        private String timezone;
        private String iata;
        private String icao;
        private String terminal;
        private String gate;
        private String delay;
        private String scheduled;
        private String estimated;
        private String actual;
        @JsonProperty("estimated_runway")
        private String estimatedRunway;
        @JsonProperty("actual_runway")
        private String actualRunway;
    }

    @Data
    public static class Arrival {
        private String airport;
        private String timezone;
        private String iata;
        private String icao;
        private String terminal;
        private String gate;
        private String baggage;
        private String delay;
        private String scheduled;
        private String estimated;
        private String actual;
        @JsonProperty("estimated_runway")
        private String estimatedRunway;
        @JsonProperty("actual_runway")
        private String actualRunway;
    }

    @Data
    public static class Airline {
        private String name;
        private String iata;
        private String icao;
    }

    @Data
    public static class Flight {
        private String number;
        private String iata;
        private String icao;
        private Codeshared codeshared;
    }

    @Data
    public static class Codeshared {
        @JsonProperty("airline_name")
        private String airlineName;
        @JsonProperty("airline_iata")
        private String airlineIata;
        @JsonProperty("airline_icao")
        private String airlineIcao;
        @JsonProperty("flight_number")
        private String flightNumber;
        @JsonProperty("flight_iata")
        private String flightIata;
        @JsonProperty("flight_icao")
        private String flightIcao;
    }

    @Data
    public static class Aircraft {
        private String registration;
        private String iata;
        private String icao;
        @JsonProperty("icao24")
        private String icao24;
    }

    @Data
    public static class Live {
        private String updated;
        private Double latitude;
        private Double longitude;
        private Double altitude;
        private Double direction;
        @JsonProperty("speed_horizontal")
        private Double speedHorizontal;
        @JsonProperty("speed_vertical")
        private Double speedVertical;
        @JsonProperty("is_ground")
        private Boolean isGround;
    }
}
