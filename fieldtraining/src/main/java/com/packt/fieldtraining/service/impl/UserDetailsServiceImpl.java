package com.packt.fieldtraining.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.packt.fieldtraining.data.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	private final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	
	
	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		LOGGER.info("[loadUserByUsername] loadUserByUsername 수행, userid : {}", userId);
		return userRepository.getByUserId(userId);
	}

}
