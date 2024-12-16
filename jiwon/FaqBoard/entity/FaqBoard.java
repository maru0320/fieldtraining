package com.fieldtraining.FaqBoard.entity;

import java.util.ArrayList;
import java.util.List;

import com.fieldtraining.data.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FaqBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardID;

    private String title;

    private String content;
  
    @Column(name = "writer_name")
    private String writerName;

    private String date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;
    
    @OneToMany(mappedBy = "faqBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FaqFile> files = new ArrayList<>();
    
    @OneToMany(mappedBy = "faqBoard", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<FaqComment> comments = new ArrayList<>();
    
    public void addComment(FaqComment comment) {
        this.comments.add(comment); // PracticeBoard의 댓글 리스트에 추가
        comment.setFaqBoard(this); // Comment에서 PracticeBoard를 설정
    }
}

