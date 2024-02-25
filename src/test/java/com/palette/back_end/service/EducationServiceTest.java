package com.palette.back_end.service;

import com.palette.back_end.dto.request.EducationRequestDTO;
import com.palette.back_end.dto.response.EducationResponseDTO;
import com.palette.back_end.entity.Education;
import com.palette.back_end.entity.User;
import com.palette.back_end.repository.EducationRepository;
import com.palette.back_end.repository.UserRepository;
import com.palette.back_end.util.exceptions.ErrorCode;
import com.palette.back_end.util.exceptions.PaletteException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EducationServiceTest {

  @Mock
  private EducationRepository educationRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  EducationService educationService;

  private final Education mockEducation1 = Education.builder()
      .educationId(1L)
      .schoolName("testUniversity")
      .major("Art")
      .description("testDescription")
      .graduationMark((float) 3.3)
      .startDate(LocalDateTime.now().minusDays(5))
      .endDate(LocalDateTime.now().minusDays(3))
      .build();

  private final Education mockEducation2 = Education.builder()
      .educationId(2L)
      .schoolName("testUniversity")
      .major("Economics")
      .description("testDescription")
      .graduationMark((float) 3.7)
      .startDate(LocalDateTime.now().minusDays(3))
      .endDate(LocalDateTime.now().minusDays(1))
      .build();

  private final User mockUser = User.builder()
      .userId(1L)
      .educations(List.of(mockEducation1, mockEducation2))
      .build();

  private final EducationRequestDTO mockRequest = EducationRequestDTO.builder()
      .schoolName("testSchool")
      .major("Art")
      .description("testDescription")
      .startDate(LocalDateTime.now().minusDays(5))
      .endDate(LocalDateTime.now().minusDays(4))
      .build();

  @Test
  void givenUserIdAndRequest_whenGetEducations_thenReturnEducationLists() {
    // given
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));

    // when
    List<EducationResponseDTO> result = educationService.getEducations(1L);

    // then
    assert(result.size() == 2);
    System.out.println(result.get(1).getEducationId());
    assert(result.get(1).getEducationId()).equals(1L);
    assert(result.get(0).getEducationId()).equals(2L);
  }

  @Test
  void givenUserIdAndRequest_whenGetEducationsAndUserNotFound_thenThrowException() {
    // given
    when(userRepository.findById(anyLong())).thenThrow(new PaletteException(ErrorCode.NOT_FOUND));

    // when
    PaletteException exception = Assertions.assertThrows(PaletteException.class, () -> {
      educationService.getEducations(1L);
    });

    // then
    assert(exception.getErrorCode()).equals(ErrorCode.NOT_FOUND);
  }

  @Test
  void givenUserIdAndRequest_whenPostEducation_thenSaveEducation() {
    // given
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));
    when(educationRepository.save(any(Education.class))).thenReturn(mockEducation1);

    // when
    EducationResponseDTO result = educationService.postEducation(1L, mockRequest);

    // then
    assert(result.getEducationId()).equals(1L);
    assert(result.getMajor()).equals(mockRequest.getMajor());
  }

  @Test
  void givenValidEducationRequest_whenPostEducationUserNotFound_thenThrowError() {
    // given
    when(userRepository.findById(anyLong())).thenThrow(new PaletteException(ErrorCode.NOT_FOUND));

    // when
    PaletteException exception = Assertions.assertThrows(PaletteException.class, () -> {
      educationService.postEducation(1L, mockRequest);
    });

    // then
    assert(exception.getErrorCode()).equals(ErrorCode.NOT_FOUND);
  }

  @Test
  void givenUserIdAndEducationIdAndValidEducationRequest_whenPutEducationEducationNotFound_thenThrowException() {
    // given
    Long userId = 1L;
    Long educationId = 1L;

    when(educationRepository.findById(anyLong())).thenThrow(new PaletteException(ErrorCode.NOT_FOUND));

    // when
    PaletteException exception = Assertions.assertThrows(PaletteException.class, () -> {
      educationService.putEducation(userId, educationId, mockRequest);
    });

    // then
    assert(exception.getErrorCode()).equals(ErrorCode.NOT_FOUND);
  }

  @Test
  void givenUserIdAndEducationIdAndValidRequest_whenPutEducationAndUserNotFound_thenThrowException() {
    // given
    Long userId = 1L;
    Long educationId = 1L;

    when(userRepository.findById(anyLong())).thenThrow(new PaletteException(ErrorCode.NOT_FOUND));
    when(educationRepository.findById(anyLong())).thenReturn(Optional.of(mockEducation1));
    when(educationRepository.save(any(Education.class))).thenReturn(mockEducation1);

    // when
    PaletteException exception = Assertions.assertThrows(PaletteException.class, () -> {
      educationService.putEducation(userId, educationId, mockRequest);
    });

    // then
    assert(exception.getErrorCode()).equals(ErrorCode.NOT_FOUND);
  }

  @Test
  void givenUserIdAndEducationIdAndValidRequest_whenPutEducation_thenSaveEducation() {
    // given
    Long userId = 1L;
    Long educationId = 1L;

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));
    when(educationRepository.findById(anyLong())).thenReturn(Optional.of(mockEducation1));
    when(educationRepository.save(any(Education.class))).thenReturn(mockEducation1);

    // when
    List<EducationResponseDTO> result = educationService.putEducation(userId, educationId, mockRequest);

    // then
    assertEquals("Art", result.get(0).getMajor());
  }

  @Test
  void givenEducationId_whenDeleteEducation_thenDeleteEducation() {
    // given
    when(educationRepository.findById(anyLong())).thenThrow(new PaletteException(ErrorCode.NOT_FOUND));

    // when
    PaletteException exception = Assertions.assertThrows(PaletteException.class, () -> {
      educationService.deleteEducation(1L);
    });

    // then
    assert(exception.getErrorCode()).equals(ErrorCode.NOT_FOUND);
  }
}