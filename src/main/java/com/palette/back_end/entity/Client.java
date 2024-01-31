package com.palette.back_end.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clients")
@SQLRestriction("is_deleted <> true")
@SQLDelete(sql = "UPDATE clients SET is_deleted = true WHERE id = ?")
public class Client extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, updatable = false)
  private Long clientId;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User userId;

  @Setter
  @Column(columnDefinition = "NOT NULL DEFAULT FALSE COMMENT '인증'")
  private boolean isAuthorized;
}
