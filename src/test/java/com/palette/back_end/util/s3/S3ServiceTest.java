package com.palette.back_end.util.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.nimbusds.common.contenttype.ContentType;
import com.palette.back_end.util.exceptions.PaletteException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

  @Mock
  AmazonS3 amazonS3;

  @InjectMocks
  S3Service s3Service;

  @Test
  void givenRightImageFile_whenUploadImagesToS3_thenReturnUrl() throws MalformedURLException {
    // given
    String fileName = "TestFile";
    MockMultipartFile file = new MockMultipartFile(
        "TestFile",
        "TestFile.jpeg",
        ContentType.IMAGE_JPEG.toString(),
        "THIS IS AN IMAGE FILE".getBytes(StandardCharsets.UTF_8)
    );

    ReflectionTestUtils.setField(s3Service, "BUCKET_NAME", "TestBucket");
    when(amazonS3.putObject(any(PutObjectRequest.class))).thenReturn(null);
    when(amazonS3.getUrl(anyString(), anyString())).thenReturn(UriComponentsBuilder.fromUriString("http://localhost:8080/testURL").build().toUri().toURL());

    // when
    String result = s3Service.uploadImageToS3(file, fileName);

    // then
    assert(result).equals("http://localhost:8080/testURL");
  }

  @Test
  void givenRightImageFile_whenUploadImagesToS3ThrowsIOException_thenReturnExceptions() throws IOException {
    // given
    String fileName = "TestFile";
    MockMultipartFile file = new MockMultipartFile(
        "TestFile",
        "TestFile.jpeg",
        ContentType.IMAGE_JPEG.toString(),
        "THIS IS AN IMAGE FILE".getBytes(StandardCharsets.UTF_8)
    );

    ReflectionTestUtils.setField(s3Service, "BUCKET_NAME", "TestBucket");
    MockMultipartFile fileSpy = Mockito.spy(file);
    doThrow(new IOException()).when(fileSpy).getInputStream();

    // when
    PaletteException result = Assertions.assertThrows(PaletteException.class, () -> {
      s3Service.uploadImageToS3(fileSpy, fileName);
    });

    // then
    assert(result.getClass()).equals(PaletteException.class);
  }

  @Test
  void givenRightImageFile_whenUploadImagesToS3ThrowsAmazonServiceException_thenReturnExceptions() {
    // given
    String fileName = "TestFile";
    MockMultipartFile file = new MockMultipartFile(
        "TestFile",
        "TestFile.jpeg",
        ContentType.IMAGE_JPEG.toString(),
        "THIS IS AN IMAGE FILE".getBytes(StandardCharsets.UTF_8)
    );

    ReflectionTestUtils.setField(s3Service, "BUCKET_NAME", "TestBucket");
    MockMultipartFile fileSpy = Mockito.spy(file);
    doThrow(new AmazonServiceException("TEST")).when(amazonS3).putObject(any(PutObjectRequest.class));

    // when
    PaletteException result = Assertions.assertThrows(PaletteException.class, () -> {
      s3Service.uploadImageToS3(fileSpy, fileName);
    });

    // then
    assert(result.getClass()).equals(PaletteException.class);
  }

  @Test
  void givenRightImageFile_whenDeleteImageFromS3_thenReturnNothing() {
    // given
    String fileName = "TestFile";
    doNothing().when(amazonS3).deleteObject(anyString(), anyString());
    ReflectionTestUtils.setField(s3Service, "BUCKET_NAME", "TestBucket");

    // then
    Assertions.assertDoesNotThrow(() -> {
      s3Service.deleteImageFromS3(fileName);
    });
  }

  @Test
  void givenRightImageFile_whenDeleteImageFromS3ThrowsAmazonServiceException_thenReturnException() {
    // given
    String fileName = "TestFile";
    doThrow(new AmazonServiceException("Test")).when(amazonS3).deleteObject(anyString(), anyString());
    ReflectionTestUtils.setField(s3Service, "BUCKET_NAME", "TestBucket");

    // when
    PaletteException result = Assertions.assertThrows(PaletteException.class, () -> {
      s3Service.deleteImageFromS3(fileName);
    });

    assert(result.getClass()).equals(PaletteException.class);
  }
}