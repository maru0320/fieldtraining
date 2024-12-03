package com.fieldtraining.calendar.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarDto {
	private Long id;
	private String title;
	private LocalDate startDate;
	private LocalDate endDate;
	private String description;
	private String role;
}
