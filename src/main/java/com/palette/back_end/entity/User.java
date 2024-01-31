package com.palette.back_end.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@SQLRestriction("is_deleted <> true")
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id = ?")
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, updatable = false)
  private Long userId;

  @Setter
  @Column(columnDefinition = "VARCHAR(55) NOT NULL COMMENT '닉네임'")
  private String userName;

  @Setter
  @Column(columnDefinition = "VARCHAR(1000) DEFAULT NULL COMMENT '프로필'")
  private String profilePictureUrl;

  @Setter
  @Column(columnDefinition = "VARCHAR(55) NOT NULL COMMENT '이메일'")
  private String email;

  @Setter
  @Column(columnDefinition = "VARCHAR(155) DEFAULT NULL COMMENT '주소'")
  private String addresses;

  @OneToMany(mappedBy = "user")
  private List<Education> educations;
}
