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
	
	// 일정 추가 또는 수정
    @PostMapping
    public ResponseEntity<?> saveEvent(@RequestBody CalendarDto calendarDto, Authentication authentication) {
    	// 로그인 여부 확인
        if (authentication == null || authentication.getPrincipal() == "anonymousUser") {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        // 현재 로그인된 사용자의 역할을 가져오기
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String role = ((User) userDetails).getRole();

        // STUDENT는 일정 생성 불가능
        if ("STUDENT".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("학생은 일정을 작성할 수 없습니다.");
        }
    	
     // 일정 생성 로직
        calendarService.createEvent(calendarDto);
        return ResponseEntity.ok("일정이 생성되었습니다.");
    }

    // 일정 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id, Authentication authentication) {
    	// 로그인 여부 확인
        if (authentication == null || authentication.getPrincipal() == "anonymousUser") {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        // 현재 로그인된 사용자의 역할을 가져오기
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String role = ((User) userDetails).getRole();

        // STUDENT는 일정 삭제 불가능
        if ("STUDENT".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("학생은 일정을 삭제할 수 없습니다.");
        }
        
        calendarService.deleteEvent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 모든 일정 조회
    @GetMapping
    public ResponseEntity<List<CalendarDto>> getAllCalendars() {
        List<CalendarDto> events = calendarService.getAllCalendars();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    // 특정 일정 조회
    @GetMapping("/{id}")
    public ResponseEntity<CalendarDto> getCalendarById(@PathVariable Long id) {
        CalendarDto event = calendarService.getCalendarById(id);
        return new ResponseEntity<>(event, HttpStatus.OK);
    } 
}