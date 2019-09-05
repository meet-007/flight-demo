package com.example.demo.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.FlightResponse;

@RestController
@RequestMapping("/flights")
public class FlightDemoRest {
	@Autowired
	Environment env;

	@GetMapping("/list")
	public List<FlightResponse> getFlights() {

		List<FlightResponse> businessRes = generateResponse(env.getProperty("flight-endpoint-business"), "business");
		List<FlightResponse> cheapRes = generateResponse(env.getProperty("flight-endpoint-cheap"), "cheap");

		businessRes.addAll(cheapRes);
		return businessRes;
	}

	private List<FlightResponse> generateResponse(String provider, String type) {
		List<FlightResponse> response = new ArrayList<FlightResponse>();

		RestTemplate restTemplate = new RestTemplate();
		String resString = restTemplate.getForObject(provider, String.class, Collections.emptyMap());

		System.out.println(resString);

		JSONObject resObj = new JSONObject(resString);

		if (resObj.has("data")) {
			try {
				JSONArray data = resObj.getJSONArray("data");

				for (int i = 0; i < data.length(); i++) {
					JSONObject obj = data.getJSONObject(i);
					FlightResponse res = new FlightResponse();

					if (type.equals("business")) {
						res.setDeparture(obj.getString("departure"));
						res.setArrival(obj.getString("arrival"));
						res.setDepartureTime(new Date(obj.getLong("departureTime")));
						res.setArrivalTime(new Date(obj.getLong("arrivalTime")));
					} else {
						String route = obj.getString("route");
						res.setDeparture(route.split("-")[0]);
						res.setArrival(route.split("-")[1]);
						res.setDepartureTime(new Date(obj.getLong("departure")));
						res.setArrivalTime(new Date(obj.getLong("arrival")));
					}
					response.add(res);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return response;
	}

}
