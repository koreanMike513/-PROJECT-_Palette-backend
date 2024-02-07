package com.palette.back_end.entity;


import com.palette.back_end.entity.enums.Districts;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "districts")
@SQLRestriction("is_deleted <> true")
@SQLDelete(sql = "UPDATE districts SET is_deleted = true WHERE id = ?")
public class District extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, updatable = false)
  private Long districtId;

  @ManyToOne
  @JoinColumn(name = "city_id")
  private City city;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "VARCHAR(55) NOT NULL COMMENT '행정 구역'")
  private Districts name;
}
