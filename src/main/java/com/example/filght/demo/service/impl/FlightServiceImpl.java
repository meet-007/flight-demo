package com.example.filght.demo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.filght.demo.dao.FlightDao;
import com.example.filght.demo.dto.BusinessFlightDto;
import com.example.filght.demo.dto.CheapFlightDto;
import com.example.filght.demo.dto.Flight;
import com.example.filght.demo.service.FlightService;

@Service
public class FlightServiceImpl implements FlightService {

	@Autowired
	FlightDao flightDao;
	@Value("${flight-endpoint-cheap}")
	private String cheap;
	@Value("${flight-endpoint-business}")
	private String business;

	@Override
	public List<Flight> getFlights() {
		List<Flight> flights = new ArrayList<Flight>();
		Object cheapFlightsResponse = flightDao.getFlights(cheap);
		List<CheapFlightDto> cheapFlights = null;
		if (cheapFlightsResponse != null) {
			cheapFlights = (List<CheapFlightDto>) cheapFlightsResponse;
			System.out.println(cheapFlights);
		}
		Object businessFlightsResponse = flightDao.getFlights(business);
		List<BusinessFlightDto> businessFlights = null;
		if (businessFlightsResponse != null) {
			businessFlights = (List<BusinessFlightDto>) businessFlightsResponse;
			System.out.println(businessFlights);
		}
		if (cheapFlights != null && !cheapFlights.isEmpty()) {
			cheapFlights.parallelStream().forEach(cheapFlight -> {
				System.out.println(cheapFlight);
				Flight flight = new Flight();
				flight.setDeparture(cheapFlight.getRoute().split("-")[0]);
				flight.setArrival(cheapFlight.getRoute().split("-")[1]);
				flight.setDepartureTime(new Date(cheapFlight.getDeparture()*1000));
				flight.setArrivalTime(new Date(cheapFlight.getArrival()*1000));
				flights.add(flight);
			});
		}
		if (businessFlights != null && !businessFlights.isEmpty()) {
			businessFlights.parallelStream().forEach(businessFlight -> {
				Flight flight = new Flight();
				flight.setDeparture(businessFlight.getDeparture());
				flight.setArrival(businessFlight.getArrival());
				flight.setDepartureTime(new Date(businessFlight.getDepartureTime()*1000));
				flight.setArrivalTime(new Date(businessFlight.getArrivalTime()*1000));
				flights.add(flight);
			});
		}
		return flights;
	}

}
