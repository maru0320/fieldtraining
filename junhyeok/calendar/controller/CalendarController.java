package com.fieldtraining.calendar.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fieldtraining.calendar.dto.CalendarDto;
import com.fieldtraining.calendar.service.CalendarService;
import com.fieldtraining.data.entity.User;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {
	private final CalendarService calendarService;
	
	public CalendarController(CalendarService calendarService) {
		this.calendarService = calendarService;
	}
	
	// 일정 추가
    @PostMapping
    public ResponseEntity<CalendarDto> addEvent(@RequestBody CalendarDto calendarDto) {
        CalendarDto createdEvent = calendarService.createEvent(calendarDto);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    // 일정 수정
    @PutMapping("/{id}")
    public ResponseEntity<CalendarDto> updateEvent(@PathVariable Long id, @RequestBody CalendarDto calendarDto) {
        calendarDto.setId(id);
        CalendarDto updatedEvent = calendarService.createEvent(calendarDto);
        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
    }

    // 일정 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        calendarService.deleteEvent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 모든 일정 조회
    @GetMapping
    public ResponseEntity<List<CalendarDto>> getAllEvents() {
        List<CalendarDto> events = calendarService.getAllCalendars();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    // 특정 일정 조회
    @GetMapping("/{id}")
    public ResponseEntity<CalendarDto> getEventById(@PathVariable Long id) {
        CalendarDto event = calendarService.getCalendarById(id);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }
}