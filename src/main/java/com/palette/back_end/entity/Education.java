package com.palette.back_end.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "educations")
@SQLRestriction("is_deleted <> true")
@SQLDelete(sql = "UPDATE education SET is_deleted = true WHERE id = ?")
public class Education extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, updatable = false)
  private Long educationId;

  @ManyToOne
  @JoinColumn(name = "artist_id")
  private Artist artist;

  @Setter
  @Column(columnDefinition = "VARCHAR(55) NOT NULL COMMENT '학교'")
  private String schoolName;

  @Setter
  @Column(columnDefinition = "VARCHAR(20) NOT NULL COMMENT '전공'")
  private String major;

  @Setter
  @Column(columnDefinition = "VARCHAR(200) NOT NULL COMMENT '설명'")
  private String description;

  @Setter
  @Column(columnDefinition = "DECIMAL(2, 1) NOT NULL COMMENT '학점'")
  private float graduationMark;

  @Setter
  @Column(columnDefinition = "TIMESTAMP NOT NULL COMMENT '기간 시작'")
  private LocalDateTime startDate;

  @Setter
  @Column(columnDefinition = "TIMESTAMP NOT NULL COMMENT '기간 끝'")
  private LocalDateTime endDate;
}
