package com.palette.back_end.dto.request;

import com.palette.back_end.entity.Portfolio;
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
public class PortfolioRequestDTO {

  @NotBlank(message = "Title cannot be null")
  @Size(max = 55, message = "Title has to be under 55 characters long")
  private String title;

  @NotBlank(message = "Description cannot be null")
  @Size(max = 200, message = "Description has to be under 200 characters long")
  private String description;

  @NotNull(message = "startDate cannot be null")
  @PastOrPresent(message = "startDate cannot be future")
  private LocalDateTime startDate;

  @NotNull(message = "endDate cannot be null")
  @PastOrPresent(message = "startDate cannot be future")
  private LocalDateTime endDate;

  public static Portfolio toEntity(User user, PortfolioRequestDTO request) {
    return Portfolio.builder()
        .user(user)
        .title(request.getTitle())
        .description(request.getDescription())
        .startDate(request.getStartDate())
        .endDate(request.getEndDate())
        .build();
  }
}
