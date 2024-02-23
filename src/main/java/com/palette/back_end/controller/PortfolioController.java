package com.palette.back_end.controller;

import com.palette.back_end.dto.request.PortfolioRequestDTO;
import com.palette.back_end.dto.response.PortfolioResponseDTO;
import com.palette.back_end.entity.User;
import com.palette.back_end.service.PortfolioService;
import com.palette.back_end.util.dto.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/portfolios")
public class PortfolioController {


  private final PortfolioService portfolioService;

  @GetMapping("/{portfolioId}")
  public ResponseDTO<PortfolioResponseDTO> getPortfolio(@PathVariable Long portfolioId) {
    return ResponseDTO.success(portfolioService.getPortfolio(portfolioId));
  }

  @GetMapping
  public ResponseDTO<List<PortfolioResponseDTO>> getPortfolios(Authentication authentication) {
    Long userId = ((User) authentication.getPrincipal()).getUserId();

    return ResponseDTO.success(portfolioService.getPortfolios(userId));
  }

  @PostMapping
  public ResponseDTO<PortfolioResponseDTO> postPortfolio(@Valid @RequestBody PortfolioRequestDTO request,
                                                         Authentication authentication) {

    Long userId = ((User) authentication.getPrincipal()).getUserId();

    return ResponseDTO.success(portfolioService.postPortfolio(userId, request));
  }

  @PostMapping("/{portfolioId}")
  public ResponseDTO<PortfolioResponseDTO> uploadPortfolioPictures(@PathVariable Long portfolioId,
                                                                  @RequestPart List<MultipartFile> files) {

    return ResponseDTO.success(portfolioService.uploadPortfolioPictures(portfolioId, files));
  }


  @PutMapping("/{portfolioId}")
  public ResponseDTO<PortfolioResponseDTO> putPortfolio(@PathVariable Long portfolioId,
                                                        @Valid @RequestBody PortfolioRequestDTO request) {
    return ResponseDTO.success(portfolioService.putPortfolio(portfolioId, request));
  }

  @DeleteMapping("/{portfolioId}")
  public ResponseDTO<Void> deletePortfolio(@PathVariable Long portfolioId) {
    portfolioService.deletePortfolio(portfolioId);
    return ResponseDTO.success();
  }

  @DeleteMapping("{portfolioId}/{portfolioPictureId}")
  public ResponseDTO<Void> deletePortfolioPicture(
      @PathVariable(name = "portfolioPictureId") Long portfolioPictureId) {
    portfolioService.deletePortfolioPicture(portfolioPictureId);
    return ResponseDTO.success();
  }
}
