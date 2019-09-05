package com.example.filght.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.filght.demo.dto.Flight;
import com.example.filght.demo.enums.OrderEnum;
import com.example.filght.demo.enums.SortByEnum;
import com.example.filght.demo.exceptions.InvalidSortParameterException;
import com.example.filght.demo.service.FlightService;

@RestController
@RequestMapping("/flights")
public class FlightDemoRest {

	@Autowired
	FlightService flightService;
	
	@GetMapping("/list")
	public List<Flight> getFlights(@RequestParam("sortBy") String sortBy,@RequestParam("order") String order) throws InvalidSortParameterException {
		if(!SortByEnum.equals(sortBy))
			throw new InvalidSortParameterException("invalid sort parameter please use arrivaltime or departuretime or arrival or departure.");
		else if(!OrderEnum.equals(order))
			throw new InvalidSortParameterException("invalid sort parameter please use asc or desc");
		return flightService.getFlights(sortBy,order);
	}

}
