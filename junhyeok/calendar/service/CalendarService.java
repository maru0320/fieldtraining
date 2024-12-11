package com.fieldtraining.calendar.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fieldtraining.calendar.dto.CalendarDto;
import com.fieldtraining.calendar.entity.Calendar;
import com.fieldtraining.calendar.repository.CalendarRepository;
import com.fieldtraining.data.entity.User;
import com.fieldtraining.data.repository.UserRepository;


@Service
public class CalendarService {
	private final CalendarRepository calendarRepository;
	private final UserRepository userRepository;
	
	public CalendarService(CalendarRepository calendarRepository, UserRepository userRepository) {
        this.calendarRepository = calendarRepository;
        this.userRepository = userRepository;
    }
	
	// DTO를 엔티티로 변환
    public Calendar mapDtoToEntity(CalendarDto dto) {
        Calendar entity = new Calendar();
        if (dto.getId() != null) {
            entity.setId(dto.getId());  // id가 존재할 경우 업데이트
        }
        entity.setTitle(dto.getTitle());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setDescription(dto.getDescription());
        entity.setRole(dto.getRole());
        return entity;
    }

    // 엔티티를 DTO로 변환
    public CalendarDto mapEntityToDto(Calendar entity) {
        return CalendarDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .description(entity.getDescription())
                .role(entity.getRole())
                .build();
    }

    // 일정 저장 (추가/수정)
    public CalendarDto createEvent(CalendarDto calendarDto) {
        Calendar calendarEvent = mapDtoToEntity(calendarDto);
        Calendar savedEvent = calendarRepository.save(calendarEvent);
        return mapEntityToDto(savedEvent);  // 저장된 엔티티를 DTO로 반환
    }

    // 일정 삭제
    public void deleteEvent(Long id) {
        calendarRepository.deleteById(id);
    }

    // 모든 일정 조회
    public List<CalendarDto> getAllCalendars() {
        return calendarRepository.findAll().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    // 특정 ID로 일정 조회
    public CalendarDto getCalendarById(Long id) {
        return calendarRepository.findById(id)
                .map(this::mapEntityToDto)
                .orElseThrow(() -> new RuntimeException("Calendar event not found"));
    }
}
