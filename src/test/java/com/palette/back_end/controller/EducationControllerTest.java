package com.palette.back_end.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palette.back_end.dto.request.EducationRequestDTO;
import com.palette.back_end.dto.response.EducationResponseDTO;
import com.palette.back_end.entity.User;
import com.palette.back_end.repository.UserRepository;
import com.palette.back_end.service.EducationService;
import com.palette.back_end.util.JwtTokenUtils;
import io.jsonwebtoken.Jwt;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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


  @Test
  @WithMockUser
  void givenRequest_whenPostEducation_returnEducationList() throws Exception {

    when(educationService.createEducation(anyLong(), any(EducationRequestDTO.class)))
        .thenReturn(EducationResponseDTO.builder().educationId(1L).build());

    mockMvc.perform(post("/api/v1/education")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(EducationRequestDTO.builder().build())))
        .andExpect(status().isOk()) // Expecting HTTP status code 200 OK
        .andReturn();
  }

  @Test
  @WithAnonymousUser
  void givenRequest_whenPostEducation_throwException() throws Exception {

    when(educationService.createEducation(anyLong(), any(EducationRequestDTO.class)))
        .thenReturn(EducationResponseDTO.builder().educationId(1L).build());

    mockMvc.perform(post("/api/v1/education")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(EducationRequestDTO.builder().build())))
        .andExpect(status().isUnauthorized())
        .andReturn();
  }


  @Test
  @WithMockUser
  void givenRequest_whenGetEducations_returnEducationList() throws Exception {

    when(educationService.getEducations(anyLong()))
        .thenReturn(List.of(EducationResponseDTO.builder().educationId(1L).build()));

    mockMvc.perform(get("/api/v1/education")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(EducationRequestDTO.builder().build())))
        .andExpect(status().isOk()) // Expecting HTTP status code 200 OK
        .andReturn();
  }

  @Test
  @WithAnonymousUser
  void givenRequest_whenGetEducations_throwException() throws Exception {

    when(educationService.getEducations(anyLong()))
        .thenReturn(List.of(EducationResponseDTO.builder().educationId(1L).build()));

    mockMvc.perform(get("/api/v1/education")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(EducationRequestDTO.builder().build())))
        .andExpect(status().isUnauthorized())
        .andReturn();
  }

  @Test
  @WithMockUser
  void givenRequest_whenPutEducation_returnEducationList() throws Exception {

    when(educationService.putEducation(anyLong(), any(EducationRequestDTO.class)))
        .thenReturn(List.of(EducationResponseDTO.builder().educationId(1L).build()));

    mockMvc.perform(put("/api/v1/education")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(EducationRequestDTO.builder().build())))
        .andExpect(status().isOk()) // Expecting HTTP status code 200 OK
        .andReturn();
  }

  @Test
  @WithAnonymousUser
  void givenRequest_whenPutEducation_throwsException() throws Exception {

    when(educationService.putEducation(anyLong(), any(EducationRequestDTO.class)))
        .thenReturn(List.of(EducationResponseDTO.builder().educationId(1L).build()));

    mockMvc.perform(put("/api/v1/education")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(EducationRequestDTO.builder().build())))
        .andExpect(status().isUnauthorized())
        .andReturn();
  }

  @Test
  @WithMockUser
  void givenRequest_whenDeleteEducation_returnVoid() throws Exception {
    doNothing().when(educationService).deleteEducation(anyLong());

    mockMvc.perform(delete("/api/v1/education/1")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithAnonymousUser
  void givenRequest_whenDeleteEducation_throwsException() throws Exception {
    doNothing().when(educationService).deleteEducation(anyLong());

    mockMvc.perform(delete("/api/v1/education/1")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
        .andExpect(status().isUnauthorized());
  }
}