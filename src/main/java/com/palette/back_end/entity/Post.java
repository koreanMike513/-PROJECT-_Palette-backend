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
@Table(name = "posts")
@SQLRestriction("is_deleted <> true")
@SQLDelete(sql = "UPDATE posts SET is_deleted = true WHERE id = ?")
public class Post extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long postId;

  @ManyToOne
  @JoinColumn(name = "client_id")
  private Client client;

  @ManyToMany
  @JoinTable(
          name = "posts_artworktypes",
          joinColumns = @JoinColumn(name = "post_id"),
          inverseJoinColumns = @JoinColumn(name = "artworktype_id")
  )
  private List<ArtworkTypes> artworkTypes;

  @ManyToMany
  @JoinTable(
          name = "posts_conditions",
          joinColumns = @JoinColumn(name = "post_id"),
          inverseJoinColumns = @JoinColumn(name = "condition_id")
  )
  private List<Condition> conditions;

  @ManyToOne
  @JoinColumn(name = "district_id")
  private District district;

  @ManyToOne
  @JoinColumn(name = "status_id")
  private Status status;

  @Setter
  @Column(columnDefinition = "VARCHAR(50) NOT NULL COMMENT '제목'")
  private String title;

  @Setter
  @Column(columnDefinition = "VARCHAR(50) NOT NULL COMMENT '본문'")
  private String body;

  @Setter
  @Column(columnDefinition = "VARCHAR(50) NOT NULL COMMENT '이메일'")
  private String email;

  @Setter
  @Column(columnDefinition = "VARCHAR(50) NOT NULL COMMENT '연락처'")
  private String contact;

  @Setter
  @Column(columnDefinition = "VARCHAR(50) NOT NULL COMMENT 'X 좌표'")
  private double xCoordinate;

  @Setter
  @Column(columnDefinition = "VARCHAR(50) NOT NULL COMMENT 'Y 좌표'")
  private double yCoordinate;
}
