package com.example.filght.demo.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.filght.demo.dto.Flight;
import com.example.filght.demo.enums.OrderEnum;
import com.example.filght.demo.enums.SortByEnum;
import com.example.filght.demo.exceptions.InvalidDateRangeException;
import com.example.filght.demo.exceptions.InvalidPaginationParameter;
import com.example.filght.demo.exceptions.InvalidSortParameterException;
import com.example.filght.demo.service.FlightService;

@RestController
@RequestMapping("/flights")
public class FlightDemoRest {

	// service instance
	@Autowired
	private FlightService flightService;

	@Autowired
	private Environment env;

	/**
	 * get the list of the flights
	 *
	 * @param sortBy
	 * @param order
	 * @param start
	 * @param end
	 * @param keyword
	 *            to filter departure and arrival
	 * @return list of flights
	 * @throws InvalidSortParameterException
	 * @throws InvalidPaginationParameter
	 * @throws InvalidDateRangeException
	 */
	@GetMapping("/list")
	public List<Flight> getFlights(@RequestParam(name = "sortBy", defaultValue = "arrivalTime") String sortBy,
			@RequestParam(name = "order", defaultValue = "asc") String order,
			@RequestParam(name = "start", defaultValue = "-1") long start,
			@RequestParam(name = "end", defaultValue = "-1") long end,
			@RequestParam(name = "keyword", defaultValue = "*") String keyword,
			@RequestParam(name = "filterBy", defaultValue = "arrivalTime") String filterBy,
			@RequestParam(name = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
			@RequestParam(name = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to)
			throws InvalidSortParameterException, InvalidPaginationParameter, InvalidDateRangeException {

		// validate parameters for valid values
		if (!SortByEnum.equals(sortBy)) {
			throw new InvalidSortParameterException(env.getProperty("invalid-sort-parameter"));
		} else if (!OrderEnum.equals(order)) {
			throw new InvalidSortParameterException(env.getProperty("invalid-sorting-order-parameter"));
		} else if (start != -1 && end != -1 && end <= start) {
			throw new InvalidPaginationParameter(env.getProperty("invalid-pagination-parameters"));
		} else if (!SortByEnum.arrivalTime.toString().equalsIgnoreCase(filterBy)
				&& !SortByEnum.departureTime.toString().equalsIgnoreCase(filterBy)) {
			throw new InvalidSortParameterException(env.getProperty("invalid-filter-parameter"));
		} else if (null != from && null != to) {

			// manage to date for filter
			Calendar cal = Calendar.getInstance();
			cal.setTime(to);
			// cal.add(Calendar.HOUR_OF_DAY, 23);
			// cal.add(Calendar.MINUTE, 59);
			// cal.add(Calendar.SECOND, 59);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			to = cal.getTime();

			// validate from and to date range
			if (from.after(to)) {
				throw new InvalidDateRangeException(env.getProperty("invalid-date-range"));
			}
		}

		// call the service for response
		return flightService.getFlights(sortBy, order, start, end, keyword, filterBy, from, to);
	}
}
