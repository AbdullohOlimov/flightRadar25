package com.example.flightradar.config;


import com.example.flightradar.telegram.FlightWatcherBot;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;



@Configuration
@RequiredArgsConstructor
@Slf4j
public class TelegramBotConfig {

    private final FlightWatcherBot flightWatcherBot;

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(flightWatcherBot);
            log.info("FlightWatcherBot registered successfully!");
        } catch (TelegramApiException e) {
            log.error("Failed to register FlightWatcherBot: {}", e.getMessage(), e);
            throw new RuntimeException("Bot registration failed", e);
        }
    }
}
