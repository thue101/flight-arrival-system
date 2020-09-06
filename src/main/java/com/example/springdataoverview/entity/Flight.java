package com.example.springdataoverview.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Flight {

    private String id;                            //Database id
    private String origin;                      //Where the flight starts
    private String destination;                // Where the flight ends
    private LocalDateTime scheduleAt;          // Time it starts
}
