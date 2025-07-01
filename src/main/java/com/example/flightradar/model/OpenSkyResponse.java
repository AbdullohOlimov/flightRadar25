package com.example.flightradar.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OpenSkyResponse {
    @JsonProperty("time")
    private Long time;

    @JsonProperty("states")
    private List<Object[]> states;
}
