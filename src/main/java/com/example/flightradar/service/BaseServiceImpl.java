package com.example.flightradar.service;

import com.example.flightradar.service.interfaces.BaseService;
import com.example.flightradar.telegram.enums.Messages;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class BaseServiceImpl implements BaseService {
    @Override
    public String getUnknownCommandMessage() {
        return Messages.UNKNOWN_MESSAGE.getText();
    }

    @Override
    public String getHelpMessage() {
        return Messages.HELP_MESSAGE.getText();
    }

    @Override
    public String getWelcomeMessage(String userName) {
        return Messages.WELCOME_TEXT.format(
                userName,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
    }
}
