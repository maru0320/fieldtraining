package com.fieldtraining.RatingBoard.entity;

import com.fieldtraining.data.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingComment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long commentID;
	
	private String content;
	
	private String date;
	
	@Column(name = "writer_name")
	private String writerName;
	
	@ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "board_id")
    private RatingBoard ratingBoard;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;


}
