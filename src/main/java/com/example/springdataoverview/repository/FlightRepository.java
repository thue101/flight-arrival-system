package com.example.springdataoverview.repository;

import com.example.springdataoverview.entity.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface FlightRepository  extends PagingAndSortingRepository<Flight /* Type of the entity*/, String> {
    List<Flight> findByOrigin(String durban);

    List<Flight> findByOriginAndDestination(String johannesburg, String destination);

    List<Flight> findByOriginIn(String ... origins);

    List<Flight> findByOriginIgnoreCase(String johannesburg);

    Page<Flight> findByOrigin(String origin, Pageable pageRequest);
}
