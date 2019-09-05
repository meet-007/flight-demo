package com.example.filght.demo.dao.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.example.filght.demo.dao.FlightDao;

@Repository
public class FlightDaoImpl implements FlightDao {

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * get flights from the provided endpoint
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getFlights(String provider) {
		if (provider != null && !provider.isEmpty()) {
			// make a rest call to provider
			Map<String, Object> flightsResponse = restTemplate.getForObject(provider, Map.class);
			if (flightsResponse != null && !flightsResponse.isEmpty() && flightsResponse.containsKey("data")) {
				// return flight data
				return flightsResponse.get("data");
			}
		}
		return null;
	}
}
