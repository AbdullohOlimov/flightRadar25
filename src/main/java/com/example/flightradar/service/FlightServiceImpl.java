package com.example.flightradar.service;

import com.example.flightradar.mapper.Mappers;
import com.example.flightradar.model.FlightData;
import com.example.flightradar.model.OpenSkyResponse;
import com.example.flightradar.model.AviationStackResponse;
import com.example.flightradar.service.interfaces.FlightService;
import com.example.flightradar.telegram.enums.Messages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightServiceImpl implements FlightService {

    private final WebClient openSkyWebClient;
    private final WebClient aviationStackWebClient;
    private final Mappers mappers;
    
    @Value("${aviationstack.api-key:}")
    private String aviationStackApiKey;

    /**
     * Enhanced flight search with multiple API fallback
     */
    @Cacheable(value = "flightData", key = "#flightNumber", unless = "#result == null")
    public FlightData getFlightInfo(String flightNumber) {
        log.info("Searching for flight: {}", flightNumber);

        CompletableFuture<FlightData> aviationFuture = CompletableFuture.supplyAsync(() -> getFlightFromAviationStack(flightNumber));

        CompletableFuture<FlightData> openSkyFuture = CompletableFuture.supplyAsync(() -> getFlightFromOpenSky(flightNumber));

        try {
            Object result = CompletableFuture.anyOf(aviationFuture, openSkyFuture)
                    .thenApply(obj -> (FlightData) obj)
                    .get();

            if (result instanceof FlightData data && data != null) {
                log.info("Flight {} found via {} API", flightNumber, data.getSource());
                return data;
            }
        } catch (Exception e) {
            log.error("Parallel API call failed for flight {}: {}", flightNumber, e.getMessage());
        }

        log.warn("Flight {} not found in any API", flightNumber);
        return null;
    }

    /**
     * Get flight data from AviationStack API
     */

    private FlightData getFlightFromAviationStack(String flightNumber) {
        if (aviationStackApiKey == null || aviationStackApiKey.isEmpty()) {
            log.debug("AviationStack API key not configured, skipping");
            return null;
        }

        try {
            AviationStackResponse response = aviationStackWebClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/flights")
                            .queryParam("access_key", aviationStackApiKey)
                            .queryParam("flight_iata", flightNumber)
                            .queryParam("limit", 1)
                            .build())
                    .retrieve()
                    .bodyToMono(AviationStackResponse.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();

            if (response != null && response.getData() != null && !response.getData().isEmpty()) {
                return mappers.mapAviationStackToFlightData(response.getData().get(0), flightNumber);
            }

        } catch (WebClientResponseException e) {
            log.error("AviationStack API error for flight {}: {} - {}",
                    flightNumber, e.getStatusCode(), e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Error fetching flight data from AviationStack for {}: {}",
                    flightNumber, e.getMessage());
        }

        return null;
    }


    private FlightData getFlightFromOpenSky(String flightNumber) {
        try {
            OpenSkyResponse response = openSkyWebClient
                    .get()
                    .uri("/states/all")
                    .retrieve()
                    .bodyToMono(OpenSkyResponse.class)
                    .timeout(Duration.ofSeconds(15))
                    .block();

            if (response != null && response.getStates() != null) {
                for (Object[] state : response.getStates()) {
                    String callsign = (String) state[1];
                    if (callsign != null && callsign.trim().equalsIgnoreCase(flightNumber.trim())) {
                        return mappers.mapOpenSkyToFlightData(state, flightNumber);
                    }
                }
            }

        } catch (Exception e) {
            log.error("Error fetching flight data from OpenSky for {}: {}",
                    flightNumber, e.getMessage());
        }

        return null;
    }


    @Override
    public String handleFlightSearch(String flightCode) {
        if (flightCode.isEmpty()) {
            return Messages.FLIGHT_CORRECT_FORM.format(flightCode);
        }

        try {
            FlightData flightData = getFlightInfo(flightCode);
            if (flightData != null) {
                return Messages.FLIGHT_INFO.format(flightData.toFlightInfoFormatArgs());
            } else {
                return Messages.FLIGHT_NOT_FOUND.format(flightCode);
            }
        } catch (Exception e) {
            log.error("Error searching flight {}: {}", flightCode, e.getMessage());
            return Messages.FLIGHT_SEARCHING_ERROR.format(flightCode);
        }
    }


}