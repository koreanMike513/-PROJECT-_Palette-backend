package com.palette.back_end.entity;

import com.palette.back_end.entity.enums.Cities;
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
@Table(name = "cities")
@SQLRestriction("is_deleted <> true")
@SQLDelete(sql = "UPDATE cities SET is_deleted = true WHERE id = ?")
public class City {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, updatable = false)
  private Long cityId;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "VARCHAR(50) NOT NULL COMMENT '도시'")
  private Cities name;
}
