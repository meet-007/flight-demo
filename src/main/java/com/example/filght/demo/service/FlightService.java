package com.example.filght.demo.service;

import java.util.Date;
import java.util.List;

import com.example.filght.demo.dto.Flight;

public interface FlightService {
	public List<Flight> getFlights(String sortBy, String order, long start, long end, String keyword, String filterBy,
			Date from, Date to);
}
