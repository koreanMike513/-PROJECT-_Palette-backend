package com.palette.back_end.controller;

import com.palette.back_end.dto.request.EducationRequestDTO;
import com.palette.back_end.dto.response.EducationResponseDTO;
import com.palette.back_end.entity.User;
import com.palette.back_end.service.EducationService;
import com.palette.back_end.util.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/education")
public class EducationController {

  private final EducationService educationService;

  @GetMapping
  public ResponseDTO<List<EducationResponseDTO>> getEducations(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return ResponseDTO.success(educationService.getEducations(user.getUserId()));
  }

  @PostMapping
  public ResponseDTO<EducationResponseDTO> createEducation(@RequestBody EducationRequestDTO request, Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return ResponseDTO.success(educationService.createEducation(user.getUserId(), request));
  }

  @PutMapping
  public ResponseDTO<List<EducationResponseDTO>> putEducation(@RequestBody EducationRequestDTO request, Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return ResponseDTO.success(educationService.putEducation(user.getUserId(), request));
  }

  @DeleteMapping("/{educationId}")
  public ResponseDTO<Void> deleteEducation(@PathVariable Long educationId) {
    educationService.deleteEducation(educationId);
    return ResponseDTO.success(null);
  }
}
