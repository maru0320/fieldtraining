package com.packt.fieldtraining.calendar.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.packt.fieldtraining.calendar.dto.CalendarDto;
import com.packt.fieldtraining.calendar.entity.Calendar;
import com.packt.fieldtraining.calendar.repository.CalendarRepository;
import com.packt.fieldtraining.common.entity.User;
import com.packt.fieldtraining.common.repository.UserRepository;

@Service
public class CalendarService {
	private final CalendarRepository calendarRepository;
	private final UserRepository userRepository;
	
	public CalendarService(CalendarRepository calendarRepository, UserRepository userRepository) {
        this.calendarRepository = calendarRepository;
        this.userRepository = userRepository;
    }
	
	public Calendar createCalendar(CalendarDto calendarDto, String username) {
		User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
		
		Calendar calendar = new Calendar();
		calendar.setTitle(calendarDto.getTitle());
		calendar.setStart(calendar.getStart());
		calendar.setEnd(calendar.getEnd());
		calendar.setDescription(calendar.getDescription());
		calendar.setUser(user);
		
		return calendarRepository.save(calendar);
	}
	
	public List<CalendarDto> getAllCalendars() {
		return calendarRepository.findAll().stream()
				.map(calendar -> new CalendarDto(calendar.getTitle(), calendar.getStart(),
						calendar.getEnd(), calendar.getDescription(), calendar.getUser().getRole()))
				.collect(Collectors.toList());
	}
	
	public void deleteEvent(Long id) {
		calendarRepository.deleteById(id);
	}
}
