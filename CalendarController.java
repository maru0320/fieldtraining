package com.packt.fieldtraining.calendar.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.packt.fieldtraining.calendar.service.CalendarService;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {
	private final CalendarService calendarService;
	
	public CalendarController(CalendarService calendarService) {
		this.calendarService = calendarService;
	}
	
	
}
