package com.fieldtraining.gallery.entity;

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
public class GalleryFile {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 파일 고유 식별자

    private String originalName; // 업로드된 파일의 원래 이름

    private String imgUrl; // 서버에 저장된 경로

    private Long fileSize; // 파일 크기 (선택)
    
    @ManyToOne(fetch = FetchType.LAZY) // Gallery와 다대일 관계
    @JoinColumn(name = "gallery_id") // 게시글 ID를 참조
    private Gallery gallery;
}
