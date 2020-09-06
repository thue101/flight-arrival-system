package com.example.springdataoverview.service;

import com.example.springdataoverview.entity.Flight;
import com.example.springdataoverview.repository.FlightRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FlightsService {
    private final FlightRepository flightRepository;

    public FlightsService (FlightRepository flightRepository){
        this.flightRepository = flightRepository;
    }

    public void saveFlight (Flight flight){
        flightRepository.save(flight);
        throw new RuntimeException("I failed");
    }

    @Transactional
    public void saveFlightTransactional(Flight flight) {
        flightRepository.save(flight);
        //test
        throw new RuntimeException("I failed");
    }
}
