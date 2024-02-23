package com.palette.back_end.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palette.back_end.util.WithMockCustomUser;
import com.palette.back_end.dto.request.PortfolioRequestDTO;
import com.palette.back_end.dto.response.PortfolioResponseDTO;
import com.palette.back_end.service.PortfolioService;
import com.palette.back_end.util.exceptions.ErrorCode;
import com.palette.back_end.util.exceptions.PaletteException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
@WebMvcTest(value = PortfolioController.class, excludeAutoConfiguration = OAuth2ClientAutoConfiguration.class)
class PortfolioControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  private PortfolioService portfolioService;

  @Value("${jwt.secret.key}")
  private String SECRET_KEY;

  private final PortfolioRequestDTO mockRequest = PortfolioRequestDTO.builder()
      .title("test")
      .description("test")
      .startDate(LocalDateTime.now().minusDays(3))
      .endDate(LocalDateTime.now().minusDays(1))
      .build();

  private final PortfolioResponseDTO mockResponse = PortfolioResponseDTO.builder()
      .portfolioId(1L)
      .title("test")
      .description("test")
      .portfolioPictures(new ArrayList<>())
      .startDate(LocalDateTime.now().minusDays(3))
      .endDate(LocalDateTime.now().minusDays(1)).build();

  private final PortfolioRequestDTO invalidMockRequest = PortfolioRequestDTO.builder()
      .title("")
      .description("test")
      .startDate(LocalDateTime.now().minusDays(3))
      .endDate(LocalDateTime.now().minusDays(1))
      .build();

  @Test
  @WithMockUser
  void givenValidPortfolioId_GetPortfolio_returnEducationList() throws Exception {
    // given
    Long portfolioId = 1L;

    PortfolioResponseDTO response = PortfolioResponseDTO.builder().build();

    when(portfolioService.getPortfolio(anyLong())).thenReturn(response);

    // when
    mockMvc.perform(get("/api/v1/portfolios/{portfolioId}", portfolioId)
            .with(csrf()))
        .andExpect(status().isOk())
        .andReturn();
  }

  @Test
  @WithMockUser
  void givenInvalidPortfolioId_GetPortfolio_ThrowNotFoundException() throws Exception {
    // given
    Long portfolioId = 1L;

    when(portfolioService.getPortfolio(anyLong())).thenThrow(new PaletteException(ErrorCode.NOT_FOUND));

    // when
    mockMvc.perform(get("/api/v1/portfolios/{portfolioId}", portfolioId)
            .with(csrf()))
        .andExpect(status().isNotFound())
        .andReturn();
  }

  @Test
  @WithAnonymousUser
  void givenValidPortfolioId_whenGetPortfolioNotSignedIn_ThrowNotFoundException() throws Exception {
    // given
    Long portfolioId = 1L;

    // when
    mockMvc.perform(get("/api/v1/portfolios/{portfolioId}", portfolioId)
            .with(csrf()))
        .andExpect(status().isUnauthorized())
        .andReturn();
  }

  @Test
  @WithMockCustomUser
  void givenValidPortfolioId_whenGetPortfolios_ThrowNotFoundException() throws Exception {
    // given
    when(portfolioService.getPortfolios(anyLong())).thenReturn(List.of(mockResponse));

    // when
    mockMvc.perform(get("/api/v1/portfolios")
            .with(csrf()))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockCustomUser
  void givenPortfolioRequest_whenPostPortfolio_thenReturnPortfolioResponseDTO() throws Exception {
    // given
    when(portfolioService.postPortfolio(anyLong(), any(PortfolioRequestDTO.class))).thenReturn(mockResponse);

    // when
    mockMvc.perform(post("/api/v1/portfolios")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(mockRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("SUCCESS"))
        .andExpect(jsonPath("$.result").exists());
  }

  @Test
  @WithMockCustomUser
  void givenInvalidPortfolioRequest_whenPostPortfolio_thenThrowError() throws Exception {
    // when
    mockMvc.perform(post("/api/v1/portfolios")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidMockRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("ERROR"))
        .andExpect(jsonPath("$.result.message").value("Title cannot be null"));
  }

  @Test
  @WithMockUser
  void givenPortfolioRequest_whenUploadPortfolioPictures() throws Exception {
    // given
    Long portfolioId = 1L;
    MockMultipartFile mockFile = new MockMultipartFile("test", "test".getBytes());
    when(portfolioService.uploadPortfolioPictures(anyLong(), any())).thenReturn(mockResponse);

    // when
    mockMvc.perform(multipart("/api/v1/portfolios/{portfolioId}", portfolioId)
            .file("files", mockFile.getBytes())
            .with(csrf())
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("SUCCESS"))
        .andExpect(jsonPath("$.result").exists());
  }

  @Test
  @WithMockUser
  void givenPortfolioIdAndRequest_WhenPutPortfolio_thenReturnPortfolioResponseDTO() throws Exception {
    // given
    Long portfolioId = 1L;

    when(portfolioService.putPortfolio(anyLong(), any(PortfolioRequestDTO.class))).thenReturn(mockResponse);

    // when
    mockMvc.perform(put("/api/v1/portfolios/{portfolioId}", portfolioId)
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(mockRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("SUCCESS"))
        .andExpect(jsonPath("$.result").exists());
  }


  @Test
  @WithMockUser
  void givenPortfolioId_whenDeletePortfolio_thenReturnOk() throws Exception {
    // given
    Long portfolioId = 1L;

    doNothing().when(portfolioService).deletePortfolio(anyLong());

    mockMvc.perform(delete("/api/v1/portfolios/{portfolioId}", portfolioId)
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("SUCCESS"))
        .andExpect(jsonPath("$.result").doesNotExist());
  }

  @Test
  @WithMockUser
  void givenPortfolioId_whenDeletePortfolioPicture_thenReturnOk() throws Exception {
    // given
    Long portfolioId = 1L;
    Long portfolioPictureId = 1L;

    doNothing().when(portfolioService).deletePortfolioPicture(anyLong());

    mockMvc.perform(delete("/api/v1/portfolios/{portfolioId}/{portfolioPictureId}", portfolioId, portfolioPictureId)
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("SUCCESS"))
        .andExpect(jsonPath("$.result").doesNotExist());
  }
}