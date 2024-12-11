package com.fieldtraining.matchingCheck.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fieldtraining.matching.entity.Match;

public interface MatchingCheckRepository extends JpaRepository<Match, Long>{

}
