package com.example.flightradar.service;

import com.example.flightradar.service.interfaces.AirportService;
import com.example.flightradar.service.interfaces.BaseService;
import com.example.flightradar.service.interfaces.FlightService;
import com.example.flightradar.telegram.enums.Commands;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommandHandler {

    private final FlightService flightService;
    private final AirportService airportService;
    private final BaseService baseService;

    public String handle(String text, String userName) {
        log.info("Handling command: {} from user: {}", text, userName);

        String command = text.toLowerCase().trim();

        if (command.equals(Commands.START.getText())) {
            return baseService.getWelcomeMessage(userName);
        }

        if (command.startsWith(Commands.FLIGHT.getText())) {
            String flightCode = command.substring(7).trim().toUpperCase();
            return flightService.handleFlightSearch(flightCode);
        }

        if (command.startsWith(Commands.SEARCH.getText())) {
            String airport = command.substring(7).trim();
            return airportService.handleAirportSearch(airport);
        }

        if (command.equals(Commands.HELP.getText())) {
            return baseService.getHelpMessage();
        }

        return baseService.getUnknownCommandMessage();
    }
}