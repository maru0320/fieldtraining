package com.fieldtraining.gallery.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fieldtraining.data.entity.User;
import com.fieldtraining.gallery.entity.Gallery;

public interface GalleryRepository extends JpaRepository<Gallery, Long>{

	List<Gallery> findByWriterId(Long writerId);

	@Query("SELECT g FROM Gallery g WHERE g.writer.id IN " +
		       "(SELECT u.id FROM User u " +
		       "LEFT JOIN u.studentDetail s " +
		       "LEFT JOIN u.teacherDetail t " +
		       "LEFT JOIN u.professorDetail p " +
		       "LEFT JOIN u.collegeManagerDetail cm " +
		       "LEFT JOIN u.schoolManagerDetail sm " +
		       "WHERE " +
		       "(s.schoolName = :affiliation OR " +
		       "t.schoolName = :affiliation OR " +
		       "p.college = :affiliation OR " +
		       "cm.college = :affiliation OR " +
		       "sm.schoolName = :affiliation))")
		List<Gallery> findByAffiliation(@Param("affiliation") String affiliation);
	
	List<Gallery> findTop5ByOrderByDateDesc();

}
