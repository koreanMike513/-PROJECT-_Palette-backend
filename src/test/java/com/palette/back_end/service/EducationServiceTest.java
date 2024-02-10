package com.palette.back_end.service;

import com.palette.back_end.dto.request.EducationRequestDTO;
import com.palette.back_end.dto.response.EducationResponseDTO;
import com.palette.back_end.entity.Education;
import com.palette.back_end.entity.User;
import com.palette.back_end.repository.EducationRepository;
import com.palette.back_end.repository.UserRepository;
import com.palette.back_end.util.exceptions.ErrorCode;
import com.palette.back_end.util.exceptions.PaletteException;
import jakarta.annotation.security.PermitAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EducationServiceTest {

  @Mock
  private EducationRepository educationRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  EducationService educationService;

  @Test
  void givenUserIdAndRequest_whenGetEducations_thenReturnEducationLists() {
    // given
    Education education = Education.builder()
        .educationId(1L)
        .major("Art")
        .build();

    User user = User.builder()
        .userId(1L)
        .educations(List.of(
            Education.builder()
                .educationId(1L)
                .startDate(LocalDateTime.now())
                .build(),
            Education.builder()
                .startDate(LocalDateTime.now().minusDays(1))
                .educationId(2L)
                .build()
        ))
        .build();

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

    // when
    List<EducationResponseDTO> result = educationService.getEducations(1L);

    // then
    assert(result.size() == 2);
    assert(result.get(0).getEducationId()).equals(2L);
    assert(result.get(1).getEducationId()).equals(1L);
  }

  @Test
  void givenUserIdAndRequest_whenUserNotFound_thenThrowException() {

    when(userRepository.findById(anyLong())).thenThrow(new PaletteException(ErrorCode.NOT_FOUND));

    // when
    PaletteException exception = Assertions.assertThrows(PaletteException.class, () -> {
      educationService.getEducations(1L);
    });

    // then
    assert(exception.getErrorCode()).equals(ErrorCode.NOT_FOUND);
  }

  @Test
  void givenUserIdAndRequest_whenCreateEducation_thenSaveEducation() {
    // given
    User user = User.builder()
        .userId(1L)
        .educations(List.of(
            Education.builder()
                .educationId(1L)
                .major("Art")
                .startDate(LocalDateTime.now())
                .build(),
            Education.builder()
                .startDate(LocalDateTime.now().minusDays(1))
                .major("Economics")
                .educationId(2L)
                .build()
        ))
        .build();

    Education education = Education.builder()
        .educationId(1L)
        .major("Art")
        .build();

    EducationRequestDTO request = EducationRequestDTO.builder()
        .educationId(1L)
        .major("Art")
        .build();

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
    when(educationRepository.save(any(Education.class))).thenReturn(education);

    // when
    EducationResponseDTO result = educationService.createEducation(1L, request);

    // then
    assert(result.getEducationId()).equals(request.getEducationId());
    assert(result.getMajor()).equals(request.getMajor());
  }

  @Test
  void givenUserIdAndRequest_whenUserNotFound_thenThrowError() {
    // given
    Education education = Education.builder()
        .educationId(1L)
        .major("Art")
        .build();

    EducationRequestDTO request = EducationRequestDTO.builder()
        .educationId(1L)
        .major("Art")
        .build();

    when(userRepository.findById(anyLong())).thenThrow(new PaletteException(ErrorCode.NOT_FOUND));

    // when
    PaletteException exception = Assertions.assertThrows(PaletteException.class, () -> {
      educationService.createEducation(1L, request);
    });

    // then
    assert(exception.getErrorCode()).equals(ErrorCode.NOT_FOUND);
  }

  @Test
  void givenUserIdAndRequest_whenEducationNotFound_thenThrowException() {
    // given
    Education education = Education.builder()
        .educationId(1L)
        .major("Economics")
        .build();

    User user = User.builder()
        .userId(1L)
        .educations(List.of(education))
        .build();

    EducationRequestDTO request = EducationRequestDTO.builder()
        .educationId(1L)
        .major("Art")
        .build();

    when(educationRepository.findById(anyLong())).thenThrow(new PaletteException(ErrorCode.NOT_FOUND));

    // when
    PaletteException exception = Assertions.assertThrows(PaletteException.class, () -> {
      educationService.putEducation(1L, request);
    });

    // then
    assert(exception.getErrorCode()).equals(ErrorCode.NOT_FOUND);
  }

  @Test
  void givenUserIdAndRequest_whenPutEducationAndUserNotFound_thenThrowException() {
    // given
    Education education = Education.builder()
        .educationId(1L)
        .major("Economics")
        .build();

    EducationRequestDTO request = EducationRequestDTO.builder()
        .educationId(1L)
        .major("Art")
        .build();

    when(userRepository.findById(anyLong())).thenThrow(new PaletteException(ErrorCode.NOT_FOUND));
    when(educationRepository.findById(anyLong())).thenReturn(Optional.of(education));
    when(educationRepository.save(any(Education.class))).thenReturn(education);

    // when
    PaletteException exception = Assertions.assertThrows(PaletteException.class, () -> {
      educationService.putEducation(1L, request);
    });

    // then
    assert(exception.getErrorCode()).equals(ErrorCode.NOT_FOUND);
  }

  @Test
  void givenUserIdAndRequest_whenPutEducation_thenSaveEducation() {
    // given
    Education education = Education.builder()
        .educationId(1L)
        .major("Economics")
        .build();

    User user = User.builder()
        .userId(1L)
        .educations(List.of(education))
        .build();

    EducationRequestDTO request = EducationRequestDTO.builder()
        .educationId(1L)
        .major("Art")
        .build();

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
    when(educationRepository.findById(anyLong())).thenReturn(Optional.of(education));
    when(educationRepository.save(any(Education.class))).thenReturn(education);

    // when
    List<EducationResponseDTO> result = educationService.putEducation(1L, request);

    // then
    assertEquals("Art", result.get(0).getMajor());
  }

  @Test
  void givenEducationId_whenDeleteEducation_thenDeleteEducation() {
    // given
    Education education = Education.builder()
        .educationId(1L)
        .major("Arts")
        .build();

    when(educationRepository.findById(anyLong())).thenThrow(new PaletteException(ErrorCode.NOT_FOUND));

    // when
    PaletteException exception = Assertions.assertThrows(PaletteException.class, () -> {
      educationService.deleteEducation(1L);
    });

    // then
    assert(exception.getErrorCode()).equals(ErrorCode.NOT_FOUND);
  }
}