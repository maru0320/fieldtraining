package com.packt.fieldtraining.matching.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.packt.fieldtraining.matching.entity.Match;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
	List<Match> findByApprovedTrue();
}
