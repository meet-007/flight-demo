package com.example.filght.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.filght.demo.dto.Flight;
import com.example.filght.demo.service.FlightService;

@RestController
@RequestMapping("/flights")
public class FlightDemoRest {

	// service instance
	@Autowired
	FlightService flightService;

	/**
	 * get the list of the flights
	 *
	 * @param keyword
	 *            to filter
	 * @return list of flights
	 */
	@GetMapping("/list")
	public List<Flight> getFlights() {
		return flightService.getFlights();
	}

}
