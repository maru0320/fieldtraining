package com.fieldtraining.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fieldtraining.data.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        if (userId == null || userId.trim().isEmpty()) {
            throw new UsernameNotFoundException("사용자 ID가 비어있습니다.");
        }

        LOGGER.info("[loadUserByUsername] loadUserByUsername 수행, userId : {}", userId);

        // userId는 실제로 사용자의 "id"일 수 있으므로, 이를 Long으로 파싱하여 id로 찾기
        Long userIdLong;
        try {
            userIdLong = Long.parseLong(userId);
        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException("유효하지 않은 userId 형식입니다.");
        }

        // id가 20인 사용자를 찾기
        return userRepository.findById(userIdLong)
                .map(user -> org.springframework.security.core.userdetails.User.builder()
                        .username(user.getUserId()) // userId를 username 필드로 매핑
                        .password(user.getPassword())
                        .roles(user.getRoles().toArray(new String[0]))
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + userId));
    }
}
