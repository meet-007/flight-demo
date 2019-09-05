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
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FlightServiceImpl implements FlightService {

	@Autowired
	FlightDao flightDao;
	@Value("${flight-endpoint-cheap}")
	private String cheap;
	@Value("${flight-endpoint-business}")
	private String business;

	@Override
	public List<Flight> getFlights(String sortBy,String order) {
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
		ObjectMapper objectMapper = new ObjectMapper();
		if (cheapFlights != null && !cheapFlights.isEmpty()) {
//			cheapFlights.parallelStream().forEach(cheapFlight -> {
			for(int i=0;i<cheapFlights.size();i++) {
//				CheapFlightDto cheapFlight = (CheapFlightDto);
				CheapFlightDto cheapFlight = objectMapper.convertValue(cheapFlights.get(i), CheapFlightDto.class);
				System.out.println(cheapFlight);
				Flight flight = new Flight();
				flight.setDeparture(cheapFlight.getRoute().split("-")[0]);
				flight.setArrival(cheapFlight.getRoute().split("-")[1]);
				flight.setDepartureTime(new Date(cheapFlight.getDeparture()*1000));
				flight.setArrivalTime(new Date(cheapFlight.getArrival()*1000));
				flights.add(flight);
			}
//			});
		}
		if (businessFlights != null && !businessFlights.isEmpty()) {
//			businessFlights.parallelStream().forEach(businessFlight -> {
			for(int i=0;i<businessFlights.size();i++) {
				BusinessFlightDto businessFlight = objectMapper.convertValue(businessFlights.get(i), BusinessFlightDto.class);
				System.out.println(businessFlight);
				Flight flight = new Flight();
				flight.setDeparture(businessFlight.getDeparture());
				flight.setArrival(businessFlight.getArrival());
				flight.setDepartureTime(new Date(businessFlight.getDepartureTime()*1000));
				flight.setArrivalTime(new Date(businessFlight.getArrivalTime()*1000));
				flights.add(flight);
			}
//			});
		}
		sortFlights(sortBy, order, flights);
		return flights;
	}
	
	private void sortFlights(String sortBy,String order,List<Flight> flights) {
		if(sortBy.equalsIgnoreCase("departure")) {
			if(order.equals("asc")) 
				flights.sort((Flight f1, Flight f2)->f1.getDeparture().compareTo(f2.getDeparture()));
			else
				flights.sort((Flight f1, Flight f2)->f2.getDeparture().compareTo(f1.getDeparture()));
		}else if(sortBy.equals("arrival")) {
			if(order.equals("asc")) 
				flights.sort((Flight f1, Flight f2)->f1.getArrival().compareTo(f2.getArrival()));
			else
				flights.sort((Flight f1, Flight f2)->f2.getArrival().compareTo(f1.getArrival()));
		}else if(sortBy.equals("departureTime")) {
			if(order.equals("asc"))
				flights.sort((Flight f1, Flight f2)->f1.getDepartureTime().compareTo(f2.getDepartureTime()));
			else
				flights.sort((Flight f1, Flight f2)->f2.getDepartureTime().compareTo(f1.getDepartureTime()));
		}else if(sortBy.equals("arrivalTime")) {
			if(order.equals("asc"))
				flights.sort((Flight f1, Flight f2)->f1.getArrivalTime().compareTo(f2.getArrivalTime()));
			else
				flights.sort((Flight f1, Flight f2)->f2.getArrivalTime().compareTo(f1.getArrivalTime()));
		}
	}

}
