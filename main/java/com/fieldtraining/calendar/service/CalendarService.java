package com.fieldtraining.calendar.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
        entity.setRole(dto.getRole()); // 전달받은 역할을 설정
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
    	// 현재 로그인된 사용자의 ID를 가져옵니다.
        User currentUser = getCurrentUser();

        // 로그인된 사용자의 역할을 DTO에 설정
        calendarDto.setRole(currentUser.getRole());

        // DTO를 엔티티로 변환하여 저장
        Calendar calendarEvent = mapDtoToEntity(calendarDto);
        Calendar savedEvent = calendarRepository.save(calendarEvent);
        return mapEntityToDto(savedEvent);
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
                .orElseThrow(() -> new RuntimeException("일정이 존재하지 않습니다"));
    }
    
 // 현재 로그인된 사용자의 User 객체를 반환하는 메서드
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 로그인되지 않은 경우 처리
        if (authentication == null || authentication.getPrincipal() == "anonymousUser") {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        // UserDetails를 사용하여 로그인된 사용자 정보 가져오기
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // userDetails.getUsername()은 userId에 해당하므로 이를 통해 사용자 엔티티를 조회
        return userRepository.findByUserId(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));
    }
}
