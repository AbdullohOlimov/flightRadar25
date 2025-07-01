package com.example.flightradar.service.interfaces;


public interface BaseService {
    String getUnknownCommandMessage();
    String getHelpMessage();
    String getWelcomeMessage(String userName);
}
