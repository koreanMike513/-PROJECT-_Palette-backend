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
import com.palette.back_end.util.s3.S3Service;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PortfolioServiceTest {

  @Mock
  private PortfolioRepository portfolioRepository;

  @Mock
  private PortfolioPictureRepository portfolioPictureRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private S3Service s3Service;

  @InjectMocks
  private PortfolioService portfolioService;

  private final Portfolio mockPortfolio = Portfolio.builder()
      .portfolioId(1L)
      .title("test")
      .description("test")
      .portfolioPictures(new ArrayList<>())
      .startDate(LocalDateTime.now().minusDays(4))
      .endDate(LocalDateTime.now().minusDays(1))
      .build();

  private final MultipartFile mockFile = new MockMultipartFile(
      "test",
      "test.jpeg",
      String.valueOf(ContentType.IMAGE_JPEG),
      new byte[1024 * 1024]);

  private final MultipartFile invalidExtensionMockFile = new MockMultipartFile(
      "test",
      "test.txt",
      String.valueOf(ContentType.IMAGE_JPEG),
      new byte[1024 * 1024]);

  private final MultipartFile invalidExtensionMockFile2 = new MockMultipartFile(
      "test",
      "test",
      String.valueOf(ContentType.IMAGE_JPEG),
      new byte[1024 * 1024]);

  private final MultipartFile invalidSizeMockFile = new MockMultipartFile(
      "test2",
      "test2.jpeg",
      String.valueOf(ContentType.IMAGE_JPEG),
      new byte[4096 * 9192]);

  private final PortfolioPicture mockPortfolioPicture = PortfolioPicture.builder()
      .portfolioPictureId(1L)
      .portfolio(mockPortfolio)
      .fileName("test")
      .url("test")
      .build();

  private final PortfolioRequestDTO mockRequest = PortfolioRequestDTO.builder()
      .title("test")
      .description("test")
      .startDate(LocalDateTime.now().minusDays(1))
      .endDate(LocalDateTime.now())
      .build();

  @Test
  void givenValidPortfolioId_whenGetPortfolio_thenReturnPortfolio() {
    // given
    Long portfolioId = 1L;

    when(portfolioRepository.findById(anyLong())).thenReturn(Optional.of(mockPortfolio));

    // when
    PortfolioResponseDTO result = portfolioService.getPortfolio(portfolioId);

    // then
    assert(result.getPortfolioId()).equals(portfolioId);
  }

  @Test
  void givenInValidPortfolioId_whenGetPortfolio_thenThrowException() {
    // given
    Long portfolioId = 1L;

    lenient().when(portfolioRepository.findById(anyLong())).thenThrow(new PaletteException(ErrorCode.NOT_FOUND));

    // when
    PaletteException exception = Assertions.assertThrows(PaletteException.class, () -> {
      portfolioService.getPortfolios(portfolioId);
    });

    // then
    assert(exception.getClass()).equals(PaletteException.class);
    assert(exception.getErrorCode()).equals(ErrorCode.NOT_FOUND);
  }

  @Test
  void givenValidUserId_whenGetPortfolios_thenReturnPortfolioList() {
    // given
    Long userId = 1L;
    LocalDateTime day1 = LocalDateTime.now().minusDays(1);
    LocalDateTime day2 = LocalDateTime.now().minusDays(2);

    User user = User.builder().userId(1L).portfolios(
        List.of(
            Portfolio.builder().portfolioId(1L).endDate(day1).build(),
            Portfolio.builder().portfolioId(2L).endDate(day2).build()))
        .build();

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

    // when
    List<PortfolioResponseDTO> result = portfolioService.getPortfolios(userId);

    // then
    assertNotNull(result);
    assert(result.size() == 2);
    assert(result.get(0).getEndDate()).equals(day1);
  }

  @Test
  void givenInValidUserId_whenGetPortfolios_thenReturnPortfolioList() {
    // given
    Long userId = 1L;
    when(userRepository.findById(anyLong())).thenThrow(new PaletteException(ErrorCode.NOT_FOUND));

    // when
    PaletteException exception = Assertions.assertThrows(PaletteException.class, () -> {
      portfolioService.getPortfolios(userId);
    });

    // then
    assert(exception.getClass()).equals(PaletteException.class);
    assert(exception.getErrorCode()).equals(ErrorCode.NOT_FOUND);
  }

  @Test
  void givenValidUserIdAndValidPortfolioRequest_whenPostPortfolio_thenReturnPortfolioResponseDTO() {
    // given
    Long userId = 1L;
    User user = User.builder().userId(1L).build();

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
    when(portfolioRepository.save(any(Portfolio.class))).thenReturn(mockPortfolio);

    // when
    PortfolioResponseDTO result = portfolioService.postPortfolio(userId, mockRequest);

    // then
    assert(result.getPortfolioId()).equals(1L);
  }

  @Test
  void givenInValidUserIdAndValidPortfolioRequest_whenPostPortfolio_thenThrowExceptions() {
    // given
    Long userId = 1L;

    when(userRepository.findById(anyLong())).thenThrow(new PaletteException(ErrorCode.NOT_FOUND));

    // when
    PaletteException exception = Assertions.assertThrows(PaletteException.class, () -> {
      portfolioService.postPortfolio(userId, mockRequest);
    });

    // then
    assert(exception.getClass()).equals(PaletteException.class);
    assert(exception.getErrorCode()).equals(ErrorCode.NOT_FOUND);
  }

  @Test
  void givenPortfolioIdAndImageFileList_whenUploadPortfolioPictures_thenReturnPortfolioResponseDTO() {
    Long portfolioId = 1L;

    when(portfolioPictureRepository.save(any(PortfolioPicture.class))).thenReturn(mockPortfolioPicture);
    when(s3Service.uploadImageToS3(any(MultipartFile.class), anyString())).thenReturn("testURL");
    when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.of(mockPortfolio));
    when(portfolioRepository.save(any(Portfolio.class))).thenReturn(mockPortfolio);

    // when
    PortfolioResponseDTO result = portfolioService.uploadPortfolioPictures(portfolioId, List.of(mockFile, mockFile));

    // then
    assert(result.getPortfolioId().equals(portfolioId));
    assertNotNull(result.getPortfolioPictures());
    assert(result.getPortfolioPictures().size() == 2);
    assert(result.getPortfolioPictures().get(0)).equals(mockPortfolioPicture);
    assert(result.getPortfolioPictures().get(1)).equals(mockPortfolioPicture);
  }

  @Test
  void givenInValidPortfolioIdAndImageFileList_whenUploadPortfolioPictures_thenThrowNotFoundException() {
    Long portfolioId = 1L;

    when(portfolioRepository.findById(portfolioId)).thenThrow(new PaletteException(ErrorCode.NOT_FOUND));

    // when
    PaletteException exception = Assertions.assertThrows(PaletteException.class, () -> {
      portfolioService.uploadPortfolioPictures(portfolioId, List.of(mockFile));
    });

    // then
    assert(exception.getClass()).equals(PaletteException.class);
    assert(exception.getErrorCode()).equals(ErrorCode.NOT_FOUND);
  }

  @Test
  void givenPortfolioIdAndInValidImageFileSize_whenUploadPortfolioPictures_thenThrowFileSizeOrExtensionException() {
    Long portfolioId = 1L;

    when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.of(mockPortfolio));

    // when
    PaletteException exception = Assertions.assertThrows(PaletteException.class, () -> {
      portfolioService.uploadPortfolioPictures(portfolioId, List.of(mockFile, invalidSizeMockFile));
    });

    // then
    assert(exception.getClass()).equals(PaletteException.class);
    assert(exception.getErrorCode()).equals(ErrorCode.INVALID_FILE_EXTENSION_OR_SIZE);
  }

  @Test
  void givenPortfolioIdAndInValidImageFileExtension_whenUploadPortfolioPictures_thenThrowFileExtensionException() {
    Long portfolioId = 1L;

    when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.of(mockPortfolio));

    // when
    PaletteException exception = Assertions.assertThrows(PaletteException.class, () -> {
      portfolioService.uploadPortfolioPictures(portfolioId, List.of(mockFile, invalidExtensionMockFile));
    });

    // then
    assert(exception.getClass()).equals(PaletteException.class);
    assert(exception.getErrorCode()).equals(ErrorCode.INVALID_FILE_EXTENSION_OR_SIZE);
  }

  @Test
  void givenPortfolioIdAndInValidImageFileExtension2_whenUploadPortfolioPictures_thenThrowFileExtensionException() {
    Long portfoiloId = 1L;

    when(portfolioRepository.findById(portfoiloId)).thenReturn(Optional.of(mockPortfolio));

    // when
    PaletteException exception = Assertions.assertThrows(PaletteException.class, () -> {
      portfolioService.uploadPortfolioPictures(portfoiloId, List.of(mockFile, invalidExtensionMockFile2));
    });

    // then
    assert(exception.getClass()).equals(PaletteException.class);
    assert(exception.getErrorCode()).equals(ErrorCode.INVALID_FILE_EXTENSION);
  }

  @Test
  void givenValidPortfolioIdAndValidUserRequest_whenPutPortfolio_thenReturn() {
    // given
    Long portfolioId = 1L;

    when(portfolioRepository.findById(anyLong())).thenReturn(Optional.of(mockPortfolio));
    when(portfolioRepository.save(any(Portfolio.class))).thenReturn(mockPortfolio);

    // when
    PortfolioResponseDTO result = portfolioService.putPortfolio(portfolioId, mockRequest);

    // then
    assert(result.getPortfolioId()).equals(portfolioId);
  }

  @Test
  void givenInValidPortfolioIdAndValidUserRequest_whenPutPortfolio_thenThrowException() {
    // given
    Long portfolioId = 1L;

    when(portfolioRepository.findById(anyLong())).thenThrow(new PaletteException(ErrorCode.NOT_FOUND));

    // when
    PaletteException exception = Assertions.assertThrows(PaletteException.class, () -> {
      portfolioService.putPortfolio(portfolioId, mockRequest);
    });

    // then
    assert(exception.getClass()).equals(PaletteException.class);
    assert(exception.getErrorCode()).equals(ErrorCode.NOT_FOUND);
  }

  @Test
  void givenValidPortfolioId_whenDeletePortfolio_thenReturnVoid() {
    // given
    Long portfolioId = 1L;

    when(portfolioRepository.findById(anyLong())).thenReturn(Optional.of(mockPortfolio));
    when(portfolioRepository.save(any(Portfolio.class))).thenReturn(mockPortfolio);

    // when
    portfolioService.deletePortfolio(portfolioId);

    // then
    assertTrue(mockPortfolio.getIsDeleted());
  }

  @Test
  void givenInValidPortfolioId_whenDeletePortfolio_thenThrowException() {
    // given
    Long portfolioId = 1L;

    when(portfolioRepository.findById(anyLong())).thenThrow(new PaletteException(ErrorCode.NOT_FOUND));

    // when
    PaletteException exception = Assertions.assertThrows(PaletteException.class, () -> {
      portfolioService.deletePortfolio(portfolioId);
    });

    // then
    assert(exception.getClass()).equals(PaletteException.class);
    assert(exception.getErrorCode()).equals(ErrorCode.NOT_FOUND);
  }

  @Test
  void givenValidPortfolioPictureId_whenDeletePortfolioPicture_thenReturnVoid() {
    // given
    Long portfolioPictureId = 1L;

    when(portfolioPictureRepository.findById(anyLong())).thenReturn(Optional.of(mockPortfolioPicture));
    when(portfolioPictureRepository.save(any(PortfolioPicture.class))).thenReturn(mockPortfolioPicture);

    // when
    portfolioService.deletePortfolioPicture(portfolioPictureId);

    // then
    assertTrue(mockPortfolioPicture.getIsDeleted());
  }

  @Test
  void givenInValidPortfolioPictureId_whenDeletePortfolioPicture_thenThrowException() {
    // given
    Long portfolioPictureId = 1L;

    when(portfolioPictureRepository.findById(anyLong())).thenThrow(new PaletteException(ErrorCode.NOT_FOUND));

    // when
    PaletteException exception = Assertions.assertThrows(PaletteException.class, () -> {
      portfolioService.deletePortfolioPicture(portfolioPictureId);
    });

    // then
    assert(exception.getClass()).equals(PaletteException.class);
    assert(exception.getErrorCode()).equals(ErrorCode.NOT_FOUND);
  }
}