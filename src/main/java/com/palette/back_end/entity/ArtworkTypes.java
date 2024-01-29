package com.palette.back_end.entity;

import com.palette.back_end.entity.enums.ArtTypes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "artwork_types")
@SQLRestriction("is_deleted <> true")
@SQLDelete(sql = "UPDATE artwork_types SET is_deleted = true WHERE id = ?")
public class ArtworkTypes extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, updatable = false)
  private Long artworkTypesId;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "VARCHAR(55) NOT NULL COMMENT '작업 종류'")
  private ArtTypes name;
}
