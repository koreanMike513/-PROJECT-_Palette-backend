package com.palette.back_end.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "portfolio_pictures")
@SQLRestriction("is_deleted <> true")
@SQLDelete(sql = "UPDATE portfolio_pictures SET is_deleted = true WHERE id = ?")
public class PortfolioPicture extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, updatable = false)
  private Long portfolioPictureId;

  @ManyToOne
  @JoinColumn(name = "portfolio_id")
  private Portfolio portfolio;

  @Setter
  @Column(columnDefinition = "VARCHAR(1000) NOT NULL COMMENT '주소'")
  private String url;
}
