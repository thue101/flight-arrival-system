package com.example.springdataoverview.querytests;

import com.example.springdataoverview.entity.Flight;
import com.example.springdataoverview.repository.FlightRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataMongoTest
public class DerivedQueryTests {

    @Autowired
    private FlightRepository flightRepository; // Wire our Flight repository so that we can make use of it during the test

    @Before // we delete everything in the database before the start of this test. The state of one test will not interfere with the other by leaving data behind
    public void setUp() {
        flightRepository.deleteAll();
    }

    @Test
    public void shouldFindFlightsFromDurban() {
        final Flight flight1 = createFlight("Durban");
        final Flight flight2 = createFlight("Durban");
        final Flight flight3 = createFlight("Lanseria");

        flightRepository.save(flight1);
        flightRepository.save(flight2);
        flightRepository.save(flight3);

        List<Flight> flightsToDurban = flightRepository.findByOrigin("Durban");

        assertThat(flightsToDurban).hasSize(2);
        assertThat(flightsToDurban.get(0)).isEqualToComparingFieldByField(flight1);
        assertThat(flightsToDurban.get(1)).isEqualToComparingFieldByField(flight2);

    }

    private Flight createFlight(String origin) {
        final Flight flight = new Flight();
        flight.setOrigin(origin);
        flight.setDestination("Chicago");
        flight.setScheduleAt(LocalDateTime.parse("2020-08-23T13:30:00"));
        return flight;
    }

    @Test
    public void shouldFindFlightsFromJohannesburgToCapeTown() {
        final Flight flight4 = createFlight("Johannesburg", "CapeTown");
        final Flight flight5 = createFlight("Johannesburg", "New York");
        final Flight flight6 = createFlight("Johannesburg", "Bulawayo");

        flightRepository.save(flight4);
        flightRepository.save(flight5);
        flightRepository.save(flight6);

        final List<Flight> JohannesburgToCapeTown = flightRepository
                .findByOriginAndDestination("Johannesburg", "CapeTown");

        assertThat(JohannesburgToCapeTown)
                .hasSize(1)
                .first()
                .isEqualToComparingFieldByField(flight4);

    }

    private Flight createFlight(String origin, String destination) {
        final Flight flight = new Flight();
        flight.setOrigin(origin);
        flight.setDestination(destination);
        flight.setScheduleAt(LocalDateTime.parse("2020-08-23T14:00:00"));
        return flight;
    }

    @Test
    public void shouldFindFlightsFromLondonOrNewYork() {
        final Flight flight7 = createFlight("London", "NewYork");
        final Flight flight8 = createFlight("Johannesburg", "New York");
        final Flight flight9 = createFlight("NewYork", "Paris");

        flightRepository.save(flight7);
        flightRepository.save(flight8);
        flightRepository.save(flight9);

        final List<Flight> londonOrNewYork = flightRepository
                .findByOriginIn("London", "NewYork");

        assertThat(londonOrNewYork).hasSize(2);
        assertThat(londonOrNewYork.get(0)).isEqualToComparingFieldByField(flight7);
        assertThat(londonOrNewYork.get(1)).isEqualToComparingFieldByField(flight9);

    }

    @Test
    public void shouldFindFlightsFromJohannesburgIgnoreCase() {
        final Flight flight10 = createFlight("JOHANNESBURG");

        flightRepository.save(flight10);

        final List<Flight> flightsToJohannesburg = flightRepository
                .findByOriginIgnoreCase("Johannesburg");

        assertThat(flightsToJohannesburg)
                .hasSize(1)
                .first()
                .isEqualToComparingFieldByField(flight10);

    }

}
