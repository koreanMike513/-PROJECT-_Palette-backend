package com.palette.back_end.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "status")
@SQLRestriction("is_deleted <> true")
@SQLDelete(sql = "UPDATE status SET is_deleted = true WHERE id = ?")
public class Status extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, updatable = false)
  private Long statusId;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "VARCHAR(100) NOT NULL COMMENT '상태'")
  private com.palette.back_end.entity.enums.Status status;
}
