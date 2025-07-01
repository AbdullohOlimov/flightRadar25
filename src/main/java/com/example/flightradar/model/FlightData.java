package com.example.flightradar.model;

import lombok.Data;

@Data
public class FlightData {
    private String flightNumber;
    private String airline;
    private String icao24;
    private String callsign;
    private String departureAirport;
    private String departureCode;
    private String arrivalAirport;
    private String arrivalCode;
    private String departureTime;
    private String arrivalTime;
    private String status;
    private Double latitude;
    private Double longitude;
    private Double altitude;
    private Double velocity;
    private Double heading;
    private String lastContact;

    public Object[] toFlightInfoFormatArgs() {
        return new Object[] {
                flightNumber,
                airline,
                departureAirport,
                departureCode,
                departureTime,
                arrivalAirport,
                arrivalCode,
                arrivalTime,
                status,
                lastContact
        };
    }

}