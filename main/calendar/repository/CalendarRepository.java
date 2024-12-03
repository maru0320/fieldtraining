package com.fieldtraining.calendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.calendar.entity.Calendar;

public interface CalendarRepository extends JpaRepository<Calendar, Long>{
	
}
