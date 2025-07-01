package com.example.flightradar.service;

import com.example.flightradar.service.interfaces.AirportService;
import com.example.flightradar.telegram.enums.Messages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class AirportServiceImpl implements AirportService {

    @Override
    public String handleAirportSearch(String airport) {
        if (airport.isEmpty()) {
            return Messages.AIRPORT_SEARCHING.getText();
        }

        try {
            // This would integrate with your airport search service
            return Messages.AIRPORT_INFO.format(
                    airport,
                    airport,
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
            );
        } catch (Exception e) {
            log.error("Error searching airport {}: {}", airport, e.getMessage());
            return Messages.AIRPORT_SEARCHING_ERR0R.format(airport);
        }
    }


}
