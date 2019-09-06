package com.example.filght.demo.service.impl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.filght.demo.dao.FlightDao;
import com.example.filght.demo.dto.BusinessFlightDto;
import com.example.filght.demo.dto.CheapFlightDto;
import com.example.filght.demo.dto.Flight;
import com.example.filght.demo.enums.OrderEnum;
import com.example.filght.demo.enums.SortByEnum;
import com.example.filght.demo.service.FlightService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FlightServiceImpl implements FlightService {

	@Autowired
	private FlightDao flightDao;

	// flight endpoints
	@Value("${flight-endpoint-cheap}")
	private String cheap;
	@Value("${flight-endpoint-business}")
	private String business;

	/**
	 * get flights from both endpoints and return resulting flight list
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Flight> getFlights(String sortBy, String order, long start, long end, String keyword, String filterBy,
			Date from, Date to) {
		List<Flight> flights = new ArrayList<Flight>();

		// get cheap flights
		Object cheapFlightsResponse = flightDao.getFlights(cheap);
		List<CheapFlightDto> cheapFlights = null;
		if (cheapFlightsResponse != null) {
			cheapFlights = (List<CheapFlightDto>) cheapFlightsResponse;
		}

		// get business flights
		Object businessFlightsResponse = flightDao.getFlights(business);
		List<BusinessFlightDto> businessFlights = null;
		if (businessFlightsResponse != null) {
			businessFlights = (List<BusinessFlightDto>) businessFlightsResponse;
		}

		// generate resulting response of cheap flights
		ObjectMapper objectMapper = new ObjectMapper();
		if (cheapFlights != null && !cheapFlights.isEmpty()) {
			for (int i = 0; i < cheapFlights.size(); i++) {
				// convert to DTO
				CheapFlightDto cheapFlight = objectMapper.convertValue(cheapFlights.get(i), CheapFlightDto.class);

				Flight flight = new Flight();
				flight.setDeparture(cheapFlight.getRoute().split("-")[0]);
				flight.setArrival(cheapFlight.getRoute().split("-")[1]);
				flight.setDepartureTime(new Date(Duration.ofSeconds(cheapFlight.getDeparture()).toMillis()));
				flight.setArrivalTime(new Date(Duration.ofSeconds(cheapFlight.getArrival()).toMillis()));
				flights.add(flight);
			}
		}

		// generate and combine resulting response of business flights
		if (businessFlights != null && !businessFlights.isEmpty()) {
			for (int i = 0; i < businessFlights.size(); i++) {
				// convert to DTO
				BusinessFlightDto businessFlight = objectMapper.convertValue(businessFlights.get(i),
						BusinessFlightDto.class);

				Flight flight = new Flight();
				flight.setDeparture(businessFlight.getDeparture());
				flight.setArrival(businessFlight.getArrival());
				flight.setDepartureTime(new Date(Duration.ofSeconds(businessFlight.getDepartureTime()).toMillis()));
				flight.setArrivalTime(new Date(Duration.ofSeconds(businessFlight.getArrivalTime()).toMillis()));
				flights.add(flight);
			}
		}

		// Code for searching based on keyword (filter by departure or arrival)
		if (!"*".endsWith(keyword)) {
			flights = flights.stream().filter(item -> item.getArrival().toString().trim().contains(keyword)
					|| item.getDeparture().toString().trim().contains(keyword)).collect(Collectors.toList());
		}

		// code to filter date range from and to for departureTime and
		// arrivalTime
		if (!filterBy.isEmpty() && null != from && null != to) {
			if (SortByEnum.arrivalTime.equals(filterBy)) {
				flights = flights.stream()
						.filter(item -> item.getArrivalTime().after(from) && item.getArrivalTime().before(to))
						.collect(Collectors.toList());
			} else if (SortByEnum.departureTime.equals(filterBy)) {
				flights = flights.stream()
						.filter(item -> item.getDepartureTime().after(from) && item.getDepartureTime().before(to))
						.collect(Collectors.toList());
			}
		}

		// Code to sort by field
		sortFlights(sortBy, order, flights);

		// code for pagination
		if (start != -1 && end != -1 && start < flights.size() && end < flights.size()) {
			flights = flights.subList((int) start - 1, (int) end - 1);
		}

		return flights;
	}

	/**
	 * apply sorting on flights response list
	 *
	 * @param sortBy
	 * @param order
	 * @param flights
	 */
	private void sortFlights(String sortBy, String order, List<Flight> flights) {
		if (sortBy.equalsIgnoreCase(SortByEnum.departure.toString())) {
			// sort by departure
			if (order.equalsIgnoreCase(OrderEnum.ASC.toString())) {
				flights.sort(Comparator.comparing(Flight::getDeparture));
			} else {
				flights.sort(Comparator.comparing(Flight::getDeparture).reversed());
			}
		} else if (sortBy.equalsIgnoreCase(SortByEnum.arrival.toString())) {
			// sort by arrival
			if (order.equalsIgnoreCase(OrderEnum.ASC.toString())) {
				flights.sort(Comparator.comparing(Flight::getArrival));
			} else {
				flights.sort(Comparator.comparing(Flight::getArrival).reversed());
			}
		} else if (sortBy.equalsIgnoreCase(SortByEnum.departureTime.toString())) {
			// sort by departureTime
			if (order.equalsIgnoreCase(OrderEnum.ASC.toString())) {
				flights.sort(Comparator.comparing(Flight::getDepartureTime));
			} else {
				flights.sort(Comparator.comparing(Flight::getDepartureTime).reversed());
			}
		} else if (sortBy.equalsIgnoreCase(SortByEnum.arrivalTime.toString())) {
			// sort by arrivalTime
			if (order.equalsIgnoreCase(OrderEnum.ASC.toString())) {
				flights.sort(Comparator.comparing(Flight::getArrivalTime));
			} else {
				flights.sort(Comparator.comparing(Flight::getArrivalTime).reversed());
			}
		}
	}
}
