package com.packt.fieldtraining.calendar.dto;

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
	private String title;
	private LocalDate start;
	private LocalDate end;
	private String Description;
	private String role;
}
