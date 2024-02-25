package com.palette.back_end.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palette.back_end.dto.request.EducationRequestDTO;
import com.palette.back_end.dto.response.EducationResponseDTO;
import com.palette.back_end.service.EducationService;
import com.palette.back_end.util.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = EducationController.class, excludeAutoConfiguration = { OAuth2ClientAutoConfiguration.class })
class EducationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  private EducationService educationService;


  private final EducationRequestDTO mockRequest = EducationRequestDTO.builder()
      .schoolName("testSchool")
      .major("Art")
      .description("testDescription")
      .startDate(LocalDateTime.now().minusDays(5))
      .endDate(LocalDateTime.now().minusDays(4))
      .build();

  private final EducationResponseDTO mockResponse = EducationResponseDTO.builder()
      .educationId(1L)
      .schoolName("testUniversity")
      .major("testMajor")
      .description("testDescription")
      .graduationMark((float) 3.3)
      .startDate(LocalDateTime.now().minusDays(5))
      .endDate(LocalDateTime.now().minusDays(3))
      .build();


  @Test
  @WithMockCustomUser
  void givenRequest_whenPostEducation_returnEducationList() throws Exception {
    when(educationService.postEducation(anyLong(), any(EducationRequestDTO.class)))
        .thenReturn(mockResponse);

    mockMvc.perform(post("/api/v1/education")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(mockResponse)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("SUCCESS"))
        .andExpect(jsonPath("$.result").exists());
  }

  @Test
  @WithAnonymousUser
  void givenRequest_whenPostEducationAndInvalidUser_throwException() throws Exception {

    when(educationService.postEducation(anyLong(), any(EducationRequestDTO.class)))
        .thenReturn(mockResponse);

    mockMvc.perform(post("/api/v1/education")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(mockRequest)))
        .andExpect(status().isUnauthorized());
  }


  @Test
  @WithMockCustomUser
  void givenRequest_whenGetEducations_returnEducationList() throws Exception {

    when(educationService.getEducations(anyLong()))
        .thenReturn(List.of(mockResponse));

    mockMvc.perform(get("/api/v1/education")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(mockRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("SUCCESS"))
        .andExpect(jsonPath("$.result").exists());
  }

  @Test
  @WithAnonymousUser
  void givenRequest_whenGetEducationsAndInvalidUser_throwException() throws Exception {

    when(educationService.getEducations(anyLong()))
        .thenReturn(List.of(mockResponse));

    mockMvc.perform(get("/api/v1/education")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(mockRequest)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockCustomUser
  void givenRequest_whenPutEducation_returnEducationList() throws Exception {
    Long educationId = 1L;

    when(educationService.putEducation(anyLong(), anyLong(), any(EducationRequestDTO.class)))
        .thenReturn(List.of(EducationResponseDTO.builder().educationId(1L).build()));

    mockMvc.perform(put("/api/v1/education/{educationId}", educationId)
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(mockRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("SUCCESS"))
        .andExpect(jsonPath("$.result").exists());
  }

  @Test
  @WithAnonymousUser
  void givenRequest_whenPutEducationAndInvalidUser_throwsException() throws Exception {

    when(educationService.putEducation(anyLong(), anyLong(), any(EducationRequestDTO.class)))
        .thenReturn(List.of(mockResponse));

    mockMvc.perform(put("/api/v1/education")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(mockRequest)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  void givenRequest_whenDeleteEducation_returnVoid() throws Exception {
    doNothing().when(educationService).deleteEducation(anyLong());

    mockMvc.perform(delete("/api/v1/education/1")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("SUCCESS"))
        .andExpect(jsonPath("$.result").doesNotExist());
  }

  @Test
  @WithAnonymousUser
  void givenRequest_whenDeleteEducationAndInvalidUser_throwsException() throws Exception {
    doNothing().when(educationService).deleteEducation(anyLong());

    mockMvc.perform(delete("/api/v1/education/1")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }
}