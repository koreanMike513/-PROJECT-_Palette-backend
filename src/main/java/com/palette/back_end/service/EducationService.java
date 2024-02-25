package com.palette.back_end.service;

import com.palette.back_end.dto.request.EducationRequestDTO;
import com.palette.back_end.dto.response.EducationResponseDTO;
import com.palette.back_end.entity.Education;
import com.palette.back_end.entity.User;
import com.palette.back_end.repository.EducationRepository;
import com.palette.back_end.repository.UserRepository;
import com.palette.back_end.util.exceptions.ErrorCode;
import com.palette.back_end.util.exceptions.PaletteException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EducationService {

  private final EducationRepository educationRepository;

  private final UserRepository userRepository;


  public List<EducationResponseDTO> getEducations(Long userId) {
    User user = findUserById(userId);
    List<Education> educations = user.getEducations();

    return educations.stream()
        .map(EducationResponseDTO::from)
        .sorted(Comparator.comparing(EducationResponseDTO::getEndDate).reversed())
        .collect(Collectors.toList());
  }

  @Transactional
  public EducationResponseDTO postEducation(Long userId, EducationRequestDTO request) {
    User user = findUserById(userId);
    Education education = EducationRequestDTO.toEntity(user, request);

    Education saved = educationRepository.save(education);

    return EducationResponseDTO.from(saved);
  }

  @Transactional
  public List<EducationResponseDTO> putEducation(Long userId, Long educationId, EducationRequestDTO request) {
    Education education = educationRepository.findById(educationId).orElseThrow(
        () -> new PaletteException(ErrorCode.NOT_FOUND));

    education.setSchoolName(request.getSchoolName());
    education.setMajor(request.getMajor());
    education.setGraduationMark(request.getGraduationMark());
    education.setDescription(request.getDescription());
    education.setStartDate(request.getStartDate());
    education.setEndDate(request.getEndDate());

    educationRepository.save(education);

    User user = findUserById(userId);
    List<Education> educations = user.getEducations();

    return educations.stream()
        .map(EducationResponseDTO::from)
        .sorted(Comparator.comparing(EducationResponseDTO::getStartDate))
        .collect(Collectors.toList());
  }

  @Transactional
  public void deleteEducation(Long educationId) {
    Education education = educationRepository.findById(educationId).orElseThrow(
        () -> new PaletteException(ErrorCode.NOT_FOUND));

    education.setIsDeleted(true);
    educationRepository.save(education);
  }

  private User findUserById(Long userId) {
    return userRepository.findById(userId).orElseThrow(
        () -> new PaletteException(ErrorCode.NOT_FOUND));
  }
}
