package com.example.flightradar.mapper;

import com.example.flightradar.model.AviationStackResponse;
import com.example.flightradar.model.FlightData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class Mappers {
    public FlightData mapAviationStackToFlightData(AviationStackResponse.FlightInfo flightInfo, String flightNumber) {
        FlightData flight = new FlightData();

        flight.setFlightNumber(flightNumber);

        // Basic flight info
        if (flightInfo.getAirline() != null) {
            flight.setAirline(flightInfo.getAirline().getName());
        }

        // Departure info
        if (flightInfo.getDeparture() != null) {
            flight.setDepartureAirport(flightInfo.getDeparture().getAirport());
            flight.setDepartureCode(flightInfo.getDeparture().getIata());
            flight.setDepartureTime(formatDateTime(flightInfo.getDeparture().getScheduled()));
        }

        // Arrival info
        if (flightInfo.getArrival() != null) {
            flight.setArrivalAirport(flightInfo.getArrival().getAirport());
            flight.setArrivalCode(flightInfo.getArrival().getIata());
            flight.setArrivalTime(formatDateTime(flightInfo.getArrival().getScheduled()));
        }

        // Live tracking data
        if (flightInfo.getLive() != null) {
            AviationStackResponse.Live live = flightInfo.getLive();
            flight.setLatitude(live.getLatitude());
            flight.setLongitude(live.getLongitude());
            flight.setAltitude(live.getAltitude());
            flight.setVelocity(live.getSpeedHorizontal());
            flight.setHeading(live.getDirection());

            if (live.getUpdated() != null) {
                flight.setLastContact(formatDateTime(live.getUpdated()));
            }
        }

        // Aircraft info
        if (flightInfo.getAircraft() != null) {
            flight.setIcao24(flightInfo.getAircraft().getIcao24());
        }

        // Status mapping
        flight.setStatus(mapFlightStatus(flightInfo.getFlightStatus()));

        return flight;
    }

    /**
     * Map OpenSky response to FlightData (existing logic)
     */
    public FlightData mapOpenSkyToFlightData(Object[] state, String flightNumber) {
        FlightData flight = new FlightData();

        flight.setFlightNumber(flightNumber);
        flight.setIcao24((String) state[0]);
        flight.setCallsign(((String) state[1]).trim());

        // Parse coordinates
        if (state[6] != null) {
            flight.setLongitude(((Number) state[6]).doubleValue());
        }
        if (state[5] != null) {
            flight.setLatitude(((Number) state[5]).doubleValue());
        }

        // Parse altitude
        if (state[7] != null) {
            flight.setAltitude(((Number) state[7]).doubleValue());
        }

        // Parse velocity
        if (state[9] != null) {
            flight.setVelocity(((Number) state[9]).doubleValue());
        }

        // Parse heading
        if (state[10] != null) {
            flight.setHeading(((Number) state[10]).doubleValue());
        }

        // Parse timestamps
        if (state[3] != null) {
            long timestamp = ((Number) state[3]).longValue();
            LocalDateTime dateTime = LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(timestamp),
                    ZoneId.systemDefault()
            );
            flight.setLastContact(dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        }

        // Set basic flight info
        flight.setAirline(determineAirline(flightNumber));
        flight.setDepartureAirport("Unknown");
        flight.setDepartureCode("N/A");
        flight.setArrivalAirport("Unknown");
        flight.setArrivalCode("N/A");
        flight.setDepartureTime("N/A");
        flight.setArrivalTime("N/A");

        // Determine status
        if (state[8] != null && ((Boolean) state[8])) {
            flight.setStatus("🛬 Yerda");
        } else {
            flight.setStatus("✈️ Uchishda");
        }

        return flight;
    }

    private String formatDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return "N/A";
        }

        try {
            // Assuming ISO format from APIs
            Instant instant = Instant.parse(dateTimeStr);
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            return localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        } catch (Exception e) {
            log.debug("Could not parse datetime: {}", dateTimeStr);
            return dateTimeStr; // Return as-is if parsing fails
        }
    }

    public String mapFlightStatus(String status) {
        if (status == null) return "Unknown";

        return switch (status.toLowerCase()) {
            case "scheduled" -> "📅 Rejalashtirilgan";
            case "active" -> "✈️ Uchishda";
            case "landed" -> "🛬 Qo'ngan";
            case "cancelled" -> "❌ Bekor qilingan";
            case "incident" -> "⚠️ Hodisa";
            case "diverted" -> "🔄 Yo'nalish o'zgartirilgan";
            default -> "❓ " + status;
        };
    }

    private String determineAirline(String flightNumber) {
        String prefix = flightNumber.length() >= 2 ? flightNumber.substring(0, 2).toUpperCase() : "";

        return switch (prefix) {
            case "UZ", "HY" -> "Uzbekistan Airways";
            case "SU" -> "Aeroflot";
            case "TK" -> "Turkish Airlines";
            case "LH" -> "Lufthansa";
            case "EK" -> "Emirates";
            case "QR" -> "Qatar Airways";
            case "EY" -> "Etihad Airways";
            case "KL" -> "KLM";
            case "AF" -> "Air France";
            case "BA" -> "British Airways";
            case "AA" -> "American Airlines";
            case "DL" -> "Delta Air Lines";
            case "UA" -> "United Airlines";
            case "AC" -> "Air Canada";
            case "CZ" -> "China Southern";
            case "MU" -> "China Eastern";
            case "CA" -> "Air China";
            case "JL" -> "Japan Airlines";
            case "NH" -> "ANA";
            case "SQ" -> "Singapore Airlines";
            case "TG" -> "Thai Airways";
            case "MH" -> "Malaysia Airlines";
            case "AI" -> "Air India";
            case "9W" -> "Jet Airways";
            case "6E" -> "IndiGo";
            default -> "Unknown Airline";
        };
    }


}
