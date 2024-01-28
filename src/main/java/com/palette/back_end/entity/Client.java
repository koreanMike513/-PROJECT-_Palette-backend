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
@Table(name = "clients")
@SQLRestriction("is_deleted <> true")
@SQLDelete(sql = "UPDATE clients SET is_deleted = true WHERE id = ?")
public class Client extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, updatable = false)
  private Long clientId;

  @Setter
  @Column(columnDefinition = "VARCHAR(55) NOT NULL COMMENT '닉네임'")
  private String userName;

  @Setter
  @Column(columnDefinition = "VARCHAR(55) NOT NULL COMMENT '이메일'")
  private String email;
}
