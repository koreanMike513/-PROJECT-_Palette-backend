package com.palette.back_end.dto.response;

import com.palette.back_end.entity.Education;
import com.palette.back_end.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EducationResponseDTO {

  private Long educationId;

  private String schoolName;

  private String major;

  private String description;

  private float graduationMark;

  private LocalDateTime startDate;

  private LocalDateTime endDate;

  public static EducationResponseDTO from(Education education) {
    return EducationResponseDTO.builder()
        .educationId(education.getEducationId())
        .schoolName(education.getSchoolName())
        .major(education.getMajor())
        .description(education.getDescription())
        .graduationMark(education.getGraduationMark())
        .startDate(education.getStartDate())
        .endDate(education.getEndDate())
        .build();
  }
}
