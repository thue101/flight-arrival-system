package com.example.springdataoverview.repositorytest;

import com.example.springdataoverview.entity.Flight;
import com.example.springdataoverview.repository.FlightRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataMongoTest
public class CrudTests {

    @Autowired
    private FlightRepository flightRepository;

    @Before
    // we delete everything in the database before the start of this test. The state of one test will not interfere with the other by leaving data behind
    public void setUp() {
        flightRepository.deleteAll();
    }

   @Test
    public void shouldPerformCRUDOperations(){
        final Flight flight = new Flight();
        flight.setOrigin("Cape Town");
        flight.setDestination("New York");
        flight.setScheduleAt(LocalDateTime.parse("2020-08-23T12:50:00"));

        flightRepository.save(flight);

        assertThat(flightRepository.findAll())
                .hasSize(1)
                .first()
                .isEqualToComparingFieldByField(flight);

        flightRepository.deleteById(flight.getId());

        assertThat(flightRepository.count()).isZero();
    }
}
