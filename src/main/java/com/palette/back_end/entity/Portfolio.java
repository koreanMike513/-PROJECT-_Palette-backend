package com.palette.back_end.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "portfolios")
@SQLRestriction("is_deleted <> true")
@SQLDelete(sql = "UPDATE portfolio SET is_deleted = true WHERE id = ?")
public class Portfolio extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, updatable = false)
  private Long portfolioId;

  @ManyToOne
  @JoinColumn(name = "artist_id")
  private User user;

  @Setter
  @Column(columnDefinition = "VARCHAR(55) NOT NULL COMMENT '제목'")
  private String title;

  @Setter
  @Column(columnDefinition = "VARCHAR(200) NOT NULL COMMENT '설명'")
  private String description;

  @Setter
  @OneToMany(mappedBy = "portfolio")
  private List<PortfolioPicture> portfolioPictures;

  @Setter
  @Column(columnDefinition = "TIMESTAMP NOT NULL COMMENT '기간 시작'")
  private LocalDateTime startDate;

  @Setter
  @Column(columnDefinition = "TIMESTAMP NOT NULL COMMENT '기간 끝'")
  private LocalDateTime endDate;
}
