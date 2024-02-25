package com.palette.back_end.dto.request;

import com.palette.back_end.entity.Education;
import com.palette.back_end.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EducationRequestDTO {

  @NotBlank(message = "school name cannot be blank")
  @Size(max = 55, message = "school name has to be under 55 characters long")
  private String schoolName;

  @NotBlank(message = "major cannot be blank")
  private String major;

  @Size(max = 500, message = "description has to be under 500 characters long")
  private String description;

  @NotNull(message = "graduationMark cannot be null")
  private float graduationMark;

  @NotNull(message = "startDate cannot be null")
  @PastOrPresent(message = "startDate cannot be future")
  private LocalDateTime startDate;

  @NotNull(message = "endDate cannot be null")
  @PastOrPresent(message = "endDate cannot be future")
  private LocalDateTime endDate;

  public static Education toEntity(User user, EducationRequestDTO request) {
    return Education.builder()
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
