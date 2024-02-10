package com.palette.back_end.dto.request;

import com.palette.back_end.dto.response.EducationResponseDTO;
import com.palette.back_end.entity.Education;
import com.palette.back_end.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EducationRequestDTO {

  private Long educationId;

  private String schoolName;

  private String major;

  private String description;

  private float graduationMark;

  private LocalDateTime startDate;

  private LocalDateTime endDate;

  public static Education toEntity(User user, EducationRequestDTO request) {
    return Education.builder()
        .educationId(request.getEducationId())
        .user(user)
        .schoolName(request.getSchoolName())
        .major(request.getMajor())
        .description(request.getDescription())
        .graduationMark(request.getGraduationMark())
        .startDate(request.getStartDate())
        .endDate(request.getEndDate())
        .build();
  }
}
