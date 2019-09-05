package com.example.filght.demo.dao;

import java.util.List;

import com.example.filght.demo.dto.BusinessFlightDto;
import com.example.filght.demo.dto.CheapFlightDto;

public interface FlightDao {
	public Object getFlights(String provider);
}
