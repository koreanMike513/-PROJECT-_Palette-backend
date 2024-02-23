package com.palette.back_end.dto.response;

import com.palette.back_end.entity.Portfolio;
import com.palette.back_end.entity.PortfolioPicture;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioResponseDTO {

  private Long portfolioId;

  private String title;

  private String description;

  private List<PortfolioPicture> portfolioPictures;

  private LocalDateTime startDate;

  private LocalDateTime endDate;

  public static PortfolioResponseDTO from(Portfolio portfolio) {
    return PortfolioResponseDTO.builder()
        .portfolioId(portfolio.getPortfolioId())
        .title(portfolio.getTitle())
        .description(portfolio.getDescription())
        .portfolioPictures(portfolio.getPortfolioPictures())
        .startDate(portfolio.getStartDate())
        .endDate(portfolio.getEndDate())
        .build();
  }
}
