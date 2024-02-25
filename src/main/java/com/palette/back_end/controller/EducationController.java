package com.palette.back_end.controller;

import com.palette.back_end.dto.request.EducationRequestDTO;
import com.palette.back_end.dto.response.EducationResponseDTO;
import com.palette.back_end.entity.User;
import com.palette.back_end.service.EducationService;
import com.palette.back_end.util.dto.ResponseDTO;
import jakarta.validation.Valid;
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
    Long userId = ((User) authentication.getPrincipal()).getUserId();
    return ResponseDTO.success(educationService.getEducations(userId));
  }

  @PostMapping
  public ResponseDTO<EducationResponseDTO> postEducation(@Valid @RequestBody EducationRequestDTO request,
                                                           Authentication authentication) {
    Long userId = ((User) authentication.getPrincipal()).getUserId();
    return ResponseDTO.success(educationService.postEducation(userId, request));
  }

  @PutMapping("/{educationId}")
  public ResponseDTO<List<EducationResponseDTO>> putEducation(@PathVariable Long educationId,
                                                              @Valid @RequestBody EducationRequestDTO request, Authentication authentication) {
    Long userId = ((User) authentication.getPrincipal()).getUserId();
    return ResponseDTO.success(educationService.putEducation(userId, educationId, request));
  }

  @DeleteMapping("/{educationId}")
  public ResponseDTO<Void> deleteEducation(@PathVariable Long educationId) {
    educationService.deleteEducation(educationId);
    return ResponseDTO.success();
  }
}
