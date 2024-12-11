package com.fieldtraining.matchingCheck.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fieldtraining.matching.entity.Match;
import com.fieldtraining.matching.repository.MatchRepository;
import com.fieldtraining.matchingCheck.dto.MatchingDto;

@Service
public class MatchingCheckService {
	@Autowired
    private MatchRepository matchRepository;

    public List<MatchingDto> getAllMatches() {
        List<Match> matches = matchRepository.findAll();

        if (matches.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "매칭 정보가 존재하지 않습니다.");
        }

        return matches.stream()
                .map(match -> MatchingDto.builder()
                        .studentName(match.getStudent().getName())
                        .teacherName(match.getTeacher().getName())
                        .matchStatus(match.getStatus().name()) // 상태를 Enum에서 String으로 변환
                        .matchApproved(match.isMatchApproved())
                        .build())
                .collect(Collectors.toList());
    }
}
