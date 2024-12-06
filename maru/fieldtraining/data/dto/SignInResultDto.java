package com.fieldtraining.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignInResultDto extends SignUpResultDto{
	
	private String token;
	
	@Builder
	public SignInResultDto(boolean success, int code, String msg, String token) {
		super(success, code, msg);
		this.token = token;
	}
			
}
