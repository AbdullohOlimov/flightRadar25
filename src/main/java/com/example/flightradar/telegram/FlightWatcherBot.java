package com.example.flightradar.telegram;

import com.example.flightradar.service.CommandHandler;
import com.example.flightradar.telegram.enums.Messages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class FlightWatcherBot extends TelegramLongPollingBot {

    private final CommandHandler commandHandler;

    @Value("${telegram.bots.username}")
    private String username;

    public FlightWatcherBot(CommandHandler commandHandler, @Value("${telegram.bots.token}") String token) {
        super(token);
        this.commandHandler = commandHandler;
        log.info("FlightWatcherBot initialized with username: {}", username);
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("Received update: {}", update);

        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getFrom().getFirstName();

            log.info("Processing message '{}' from user {} (chat: {})", text, userName, chatId);

            try {
                String response = commandHandler.handle(text, userName);
                sendMessage(chatId, response);
            } catch (Exception e) {
                log.error("Error processing command: {}", e.getMessage(), e);
                sendMessage(chatId, Messages.ERROR_MESSAGE.getText());
            }
        }
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .parseMode("HTML")
                .build();

        try {
            execute(message);
            log.info("Message sent successfully to chat: {}", chatId);
        } catch (TelegramApiException e) {
            log.error("Error sending message to chat {}: {}", chatId, e.getMessage(), e);
        }
    }
}