package com.example.filght.demo.dao.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.example.filght.demo.dao.FlightDao;
import com.example.filght.demo.dto.BusinessFlightDto;
import com.example.filght.demo.dto.CheapFlightDto;

@Repository
public class FlightDaoImpl implements FlightDao {

	@Autowired
	private RestTemplate restTemplate;
	@Value("${flight-endpoint-cheap}")
	private String cheap;
	@Value("${flight-endpoint-business}")
	private String business;
	
	public Object getFlights(String provider) {
		Map<String,Object> flightsResponse = restTemplate.getForObject(provider,Map.class);
		if(flightsResponse!=null&&!flightsResponse.isEmpty()&&flightsResponse.containsKey("data")) {
			return flightsResponse.get("data");
		}
		return null;
	}
}
