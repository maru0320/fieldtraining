package com.packt.fieldtraining.match.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.packt.fieldtraining.common.entity.Student;
import com.packt.fieldtraining.match.entity.Match;

public interface MatchRepository extends JpaRepository<Match, Long>{
}
