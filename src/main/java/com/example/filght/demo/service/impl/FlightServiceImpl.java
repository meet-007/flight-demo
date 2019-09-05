package com.example.filght.demo.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
	FlightDao flightDao;

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
	public List<Flight> getFlights(String sortBy, String order, long start, long end, String keyword) {
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
				CheapFlightDto cheapFlight = objectMapper.convertValue(cheapFlights.get(i), CheapFlightDto.class);
				Flight flight = new Flight();
				flight.setDeparture(cheapFlight.getRoute().split("-")[0]);
				flight.setArrival(cheapFlight.getRoute().split("-")[1]);
				flight.setDepartureTime(new Date(cheapFlight.getDeparture() * 1000));
				flight.setArrivalTime(new Date(cheapFlight.getArrival() * 1000));
				flights.add(flight);
			}
		}

		// generate and combine resulting response of business flights
		if (businessFlights != null && !businessFlights.isEmpty()) {
			for (int i = 0; i < businessFlights.size(); i++) {
				BusinessFlightDto businessFlight = objectMapper.convertValue(businessFlights.get(i),
						BusinessFlightDto.class);
				Flight flight = new Flight();
				flight.setDeparture(businessFlight.getDeparture());
				flight.setArrival(businessFlight.getArrival());
				flight.setDepartureTime(new Date(businessFlight.getDepartureTime() * 1000));
				flight.setArrivalTime(new Date(businessFlight.getArrivalTime() * 1000));
				flights.add(flight);
			}
		}

		flights.stream().forEach(action -> {
			System.out.println("Arrival" + action.getArrival());
			System.out.println("Departure" + action.getDeparture());
		});
		// Code for searching based on keyword
		if (keyword != "*") {
			flights = flights.stream().filter(item -> item.getArrival().toString().trim().contains(keyword)
					|| item.getDeparture().toString().trim().contains(keyword)).collect(Collectors.toList());
		}
		// Code for searching based on keyword
		sortFlights(sortBy, order, flights);
		if (start != -1 && end != -1 && start < flights.size() && end < flights.size()) {
			flights = flights.subList((int) start - 1, (int) end - 1);
		}
		return flights;
	}

	private void sortFlights(String sortBy, String order, List<Flight> flights) {
		if (sortBy.equalsIgnoreCase(SortByEnum.departure.toString())) {
			if (order.equalsIgnoreCase(OrderEnum.ASC.toString())) {
				flights.sort((Flight f1, Flight f2) -> f1.getDeparture().compareTo(f2.getDeparture()));
			} else {
				flights.sort((Flight f1, Flight f2) -> f2.getDeparture().compareTo(f1.getDeparture()));
			}
		} else if (sortBy.equalsIgnoreCase(SortByEnum.arrival.toString())) {
			if (order.equalsIgnoreCase(OrderEnum.ASC.toString())) {
				flights.sort((Flight f1, Flight f2) -> f1.getArrival().compareTo(f2.getArrival()));
			} else {
				flights.sort((Flight f1, Flight f2) -> f2.getArrival().compareTo(f1.getArrival()));
			}
		} else if (sortBy.equalsIgnoreCase(SortByEnum.departureTime.toString())) {
			if (order.equalsIgnoreCase(OrderEnum.ASC.toString())) {
				flights.sort((Flight f1, Flight f2) -> f1.getDepartureTime().compareTo(f2.getDepartureTime()));
			} else {
				flights.sort((Flight f1, Flight f2) -> f2.getDepartureTime().compareTo(f1.getDepartureTime()));
			}
		} else if (sortBy.equalsIgnoreCase(SortByEnum.arrivalTime.toString())) {
			if (order.equalsIgnoreCase(OrderEnum.ASC.toString())) {
				flights.sort((Flight f1, Flight f2) -> f1.getArrivalTime().compareTo(f2.getArrivalTime()));
			} else {
				flights.sort((Flight f1, Flight f2) -> f2.getArrivalTime().compareTo(f1.getArrivalTime()));
			}
		}
	}

	private static void formatTime(Long time) {
		LocalDateTime dateTime = LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.UTC);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss", Locale.US);
		String formattedDate = dateTime.format(formatter);
		System.out.println(formattedDate);
	}
}
