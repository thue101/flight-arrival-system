package com.example.springdataoverview.pagingandsortingtests;

import com.example.springdataoverview.entity.Flight;
import com.example.springdataoverview.repository.FlightRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.Sort.Direction.DESC;

@RunWith(SpringRunner.class)
@DataMongoTest
public class PagingAndSortingTests {

    @Autowired
    private FlightRepository flightRepository;

    @Before
    public void setUp() {
        flightRepository.deleteAll();
    }

    @Test
    public void shouldSortFlightByDestination() {
        final Flight flight1 = createFlight("CapeTown");
        final Flight flight2 = createFlight("Lanseria");
        final Flight flight3 = createFlight("Durban");
        final Flight flight4 = createFlight("Chicago");
        final Flight flight5 = createFlight("London");
        final Flight flight6 = createFlight("Sydney");

        flightRepository.save(flight1);
        flightRepository.save(flight2);
        flightRepository.save(flight3);
        flightRepository.save(flight4);
        flightRepository.save(flight5);
        flightRepository.save(flight6);

        final Iterable<Flight> flights = flightRepository.findAll(Sort.by("destination"));

        assertThat(flights).hasSize(6);

        final Iterator<Flight> iterator = flights.iterator();

        assertThat(iterator.next().getDestination()).isEqualTo("CapeTown");
        assertThat(iterator.next().getDestination()).isEqualTo("Chicago");
        assertThat(iterator.next().getDestination()).isEqualTo("Durban");
        assertThat(iterator.next().getDestination()).isEqualTo("Lanseria");
        assertThat(iterator.next().getDestination()).isEqualTo("London");
        assertThat(iterator.next().getDestination()).isEqualTo("Sydney");

    }

    private Flight createFlight(String destination) {
        Flight flight = new Flight();
        flight.setOrigin("CapeTown");
        flight.setDestination(destination);
        flight.setScheduleAt(LocalDateTime.parse("2020-08-23T17:00:00"));
        return flight;
    }

    @Test
    public void shouldSortFlightByScheduleAndThenName() {
        final LocalDateTime now = LocalDateTime.now();
        final Flight johannesburg1 = createFlight("Johannesburg", now);
        final Flight johannesburg2 = createFlight("Johannesburg", now.plusHours(3));
        final Flight johannesburg3 = createFlight("Johannesburg", now.plusHours(1));
        final Flight durban1 = createFlight("Durban", now.minusHours(1));
        final Flight durban2 = createFlight("Durban", now.minusHours(3));

        flightRepository.save(johannesburg1);
        flightRepository.save(johannesburg2);
        flightRepository.save(johannesburg3);
        flightRepository.save(durban1);
        flightRepository.save(durban2);

        final Iterable<Flight> flights = flightRepository.findAll(Sort.by("destination", "scheduleAt"));

        assertThat(flights).hasSize(5);

        final Iterator<Flight> iterator = flights.iterator();

        assertThat(iterator.next()).isEqualToComparingFieldByField(durban2);
        assertThat(iterator.next()).isEqualToComparingFieldByField(durban1);
        assertThat(iterator.next()).isEqualToComparingFieldByField(johannesburg1);
        assertThat(iterator.next()).isEqualToComparingFieldByField(johannesburg3);
        assertThat(iterator.next()).isEqualToComparingFieldByField(johannesburg2);


    }

    private Flight createFlight(String destination, LocalDateTime scheduleAt) {
        Flight flight = new Flight();
        flight.setOrigin("Johannesburg");
        flight.setDestination(destination);
        flight.setScheduleAt(scheduleAt);
        return flight;

    }

    @Test
    public void shouldPageResults() {
        for (int i = 0; i < 50; i++) {
            flightRepository.save(createFlight(String.valueOf(i)));
        }
        final Page<Flight> page = flightRepository.findAll(PageRequest.of(2, 5));

        assertThat(page.getTotalElements()).isEqualTo(50);
        assertThat(page.getNumberOfElements()).isEqualTo(5);
        assertThat(page.getTotalPages()).isEqualTo(10);
        assertThat(page.getContent())
                .extracting(Flight::getDestination)
                .containsExactly("10","11","12","13","14");
    }
    @Test
    public void shouldPageAndSortResults() {
        for (int i = 0; i < 50; i++) {
            flightRepository.save(createFlight(String.valueOf(i)));
        }
        final Page<Flight> page = flightRepository.findAll(PageRequest.of(2, 5,Sort.by(DESC, "destination")));

        assertThat(page.getTotalElements()).isEqualTo(50);
        assertThat(page.getNumberOfElements()).isEqualTo(5);
        assertThat(page.getTotalPages()).isEqualTo(10);
        assertThat(page.getContent())
                .extracting(Flight::getDestination)
                .containsExactly("44","43","42","41","40");
    }
    @Test
    public void shouldPageAndSortADerivedQuery() {
        for (int i = 0; i < 10; i++) {
            final Flight flight = createFlight(String.valueOf(i));
            flight.setOrigin("Johannesburg");
            flightRepository.save(flight);
        }
        for (int i = 0; i < 10; i++) {
            final Flight flight = createFlight(String.valueOf(i));
            flight.setOrigin("Durban");
            flightRepository.save(flight);
        }
        final Page<Flight> page = flightRepository
                .findByOrigin("Johannesburg",
                        PageRequest.of(0, 5,Sort.by(DESC, "destination")));

        assertThat(page.getTotalElements()).isEqualTo(10);
        assertThat(page.getNumberOfElements()).isEqualTo(5);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getContent())
                .extracting(Flight::getDestination)
                .containsExactly("9","8","7","6","5");
    }
}
