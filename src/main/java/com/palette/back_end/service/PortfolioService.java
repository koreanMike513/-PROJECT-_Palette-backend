package com.palette.back_end.service;

import com.palette.back_end.dto.request.PortfolioRequestDTO;
import com.palette.back_end.dto.response.PortfolioResponseDTO;
import com.palette.back_end.entity.Portfolio;
import com.palette.back_end.entity.PortfolioPicture;
import com.palette.back_end.entity.User;
import com.palette.back_end.repository.PortfolioPictureRepository;
import com.palette.back_end.repository.PortfolioRepository;
import com.palette.back_end.repository.UserRepository;
import com.palette.back_end.util.exceptions.ErrorCode;
import com.palette.back_end.util.exceptions.PaletteException;
import com.palette.back_end.util.file.FileUtil;
import com.palette.back_end.util.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioService {

  private final PortfolioRepository portfolioRepository;

  private final PortfolioPictureRepository portfolioPictureRepository;

  private final UserRepository userRepository;

  private final S3Service s3Service;


  public PortfolioResponseDTO getPortfolio(Long portfolioId) {
    Portfolio portfolio = findPortfolioById(portfolioId);

    return PortfolioResponseDTO.from(portfolio);
  }

  public List<PortfolioResponseDTO> getPortfolios(Long userId) {
    User user = findUserById(userId);
    List<Portfolio> portfolios = user.getPortfolios();

    return portfolios.stream()
        .map(PortfolioResponseDTO::from)
        .sorted(Comparator.comparing(PortfolioResponseDTO::getEndDate).reversed())
        .collect(Collectors.toList());
  }

  @Transactional
  public PortfolioResponseDTO postPortfolio(Long userId, PortfolioRequestDTO request) {
    User user = findUserById(userId);
    Portfolio saved = portfolioRepository.save(PortfolioRequestDTO.toEntity(user, request));

    return PortfolioResponseDTO.from(saved);
  }

  @Transactional
  public PortfolioResponseDTO uploadPortfolioPictures(Long portfolioId, List<MultipartFile> files) {
    Portfolio portfolio = findPortfolioById(portfolioId);

    if (!FileUtil.validateImages(files)) {
      throw new PaletteException(ErrorCode.INVALID_FILE_EXTENSION_OR_SIZE);
    }

    for (MultipartFile file : files) {
      String fileName = FileUtil.generateFileName(file);
      String url = s3Service.uploadImageToS3(file, fileName);

      portfolio.getPortfolioPictures().add(
          portfolioPictureRepository.save(
          PortfolioPicture.builder()
              .portfolioPictureId(portfolioId)
              .fileName(fileName)
              .url(url)
              .build()));
    }

    return PortfolioResponseDTO.from(portfolioRepository.save(portfolio));
  }

  @Transactional
  public PortfolioResponseDTO putPortfolio(Long portfolioId, PortfolioRequestDTO request) {
    Portfolio portfolio = findPortfolioById(portfolioId);

    portfolio.setTitle(request.getTitle());
    portfolio.setDescription(request.getDescription());
    portfolio.setStartDate(request.getStartDate());
    portfolio.setEndDate(request.getEndDate());

    return PortfolioResponseDTO.from(portfolioRepository.save(portfolio));
  }

  public void deletePortfolio(Long portfolioId) {
    Portfolio portfolio = findPortfolioById(portfolioId);

    portfolio.setIsDeleted(true);
    portfolioRepository.save(portfolio);
  }

  public void deletePortfolioPicture(Long portfolioPictureId) {
    PortfolioPicture portfolioPicture = portfolioPictureRepository.findById(portfolioPictureId).orElseThrow(
        () -> new PaletteException(ErrorCode.NOT_FOUND)
    );

    portfolioPicture.setIsDeleted(true);
    portfolioPictureRepository.save(portfolioPicture);
  }

  private Portfolio findPortfolioById(Long portfolioId) {
    return portfolioRepository.findById(portfolioId).orElseThrow(
        () -> new PaletteException(ErrorCode.NOT_FOUND));
  }

  private User findUserById(Long userId) {
    return userRepository.findById(userId).orElseThrow(
        () -> new PaletteException(ErrorCode.NOT_FOUND));
  }
}
