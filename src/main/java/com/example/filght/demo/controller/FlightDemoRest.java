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
import com.example.filght.demo.exceptions.InvalidPaginationParameter;
import com.example.filght.demo.exceptions.InvalidSortParameterException;
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
	public List<Flight> getFlights(@RequestParam(name = "sortBy", defaultValue = "arrivalTime") String sortBy,
			@RequestParam(name = "order", defaultValue = "asc") String order,
			@RequestParam(name = "start", defaultValue = "-1") long start,
			@RequestParam(name = "end", defaultValue = "-1") long end,
			@RequestParam(name = "keyword", defaultValue = "*") String keyword)
			throws InvalidSortParameterException, InvalidPaginationParameter {
		if (!SortByEnum.equals(sortBy)) {
			throw new InvalidSortParameterException(
					"invalid sort parameter please use arrivaltime or departuretime or arrival or departure.");
		} else if (!OrderEnum.equals(order)) {
			throw new InvalidSortParameterException("invalid sort parameter please use asc or desc");
		} else if (start != -1 && end != -1 && end <= start) {
			throw new InvalidPaginationParameter("end must be greater than start");
		}
		return flightService.getFlights(sortBy, order, start, end, keyword);
	}

}
