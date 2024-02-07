package com.palette.back_end.entity;

import com.palette.back_end.entity.enums.Conditions;
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
@Table(name = "conditions")
@SQLRestriction("is_deleted <> true")
@SQLDelete(sql = "UPDATE conditions SET is_deleted = true WHERE id = ?")
public class Condition extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long conditionId;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "VARCHAR(50) NOT NULL COMMENT '근로 조건'")
  private Conditions name;
}
