package com.example.filght.demo.service;

import java.util.List;

import com.example.filght.demo.dto.Flight;

public interface FlightService {
	public List<Flight> getFlights (String sortBy,String order);
}
