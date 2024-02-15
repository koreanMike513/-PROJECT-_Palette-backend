package com.palette.back_end.util.file;


import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilTest {

  @Test
  void givenFile_whenGenerateFileName_thenReturnFileName() {
    // given
    MultipartFile file = new MockMultipartFile(
        "test",
        "test.jpeg",
        String.valueOf(ContentType.IMAGE_JPEG),
        "picture".getBytes(StandardCharsets.UTF_8));

    // when
    String result = FileUtil.generateFileName(file);

    // then
    assertNotNull(result);
    assert(result.substring(result.lastIndexOf("."))).equals(".jpeg");
  }

  @Test
  void givenFile_whenValidateImage_thenReturnTrue() {
    // given
    MultipartFile file = new MockMultipartFile(
        "test",
        "test.jpeg",
        String.valueOf(ContentType.IMAGE_JPEG),
        new byte[1024 * 1024]);

    // when
    boolean result = FileUtil.validateImages(List.of(file));

    // then
    assertTrue(result);
  }

  @Test
  void givenFiles_whenValidateImage_thenReturnTrue() {
    // given
    MultipartFile file1 = new MockMultipartFile(
        "test1",
        "test1.jpeg",
        String.valueOf(ContentType.IMAGE_JPEG),
        new byte[1024 * 1024]);

    MultipartFile file2 = new MockMultipartFile(
        "test2",
        "test2.jpeg",
        String.valueOf(ContentType.IMAGE_JPEG),
        new byte[1024 * 1024]);

    // when
    boolean result = FileUtil.validateImages(List.of(file1, file2));

    // then
    assertTrue(result);
  }

  @Test
  void givenOverSizeFile_whenValidateImage_thenReturnFalse() {
    // given
    MultipartFile file = new MockMultipartFile(
        "test",
        "test.jpeg",
        String.valueOf(ContentType.IMAGE_JPEG),
        new byte[4096 * 9192]);

    // when
    boolean result = FileUtil.validateImages(List.of(file));

    // then
    assertFalse(result);
  }

  @Test
  void givenInvalidFileExtension_whenValidateImage_thenReturnFalse() {
    // given
    MultipartFile file = new MockMultipartFile(
        "test",
        "test.txt",
        String.valueOf(ContentType.TEXT_PLAIN),
        new byte[1024 * 1024]);

    // when
    boolean result = FileUtil.validateImages(List.of(file));

    // then
    assertFalse(result);
  }

  @Test
  void givenNoFile_whenValidateImage_thenReturnFalse() {
    MockMultipartFile file = new MockMultipartFile(
        "test",
        "test.txt",
        String.valueOf(ContentType.TEXT_PLAIN),
        new byte[0]
    );
    // when
    boolean result = FileUtil.validateImages(List.of(file));

    // then
    assertFalse(result);
  }

  @Test
  void givenOneInvalidFileExtensionInFiles_whenValidateImage_thenReturnFalse() {
    // given
    MultipartFile file1 = new MockMultipartFile(
        "test1",
        "test1.jpeg",
        String.valueOf(ContentType.IMAGE_JPEG),
        new byte[1024 * 1024]);

    MultipartFile file2 = new MockMultipartFile(
        "test2",
        "test2.txt",
        String.valueOf(ContentType.TEXT_PLAIN),
        new byte[1024 * 1024]);

    // when
    boolean result = FileUtil.validateImages(List.of(file1, file2));

    // then
    assertFalse(result);
  }

  @Test
  void givenOneInvalidFileSizeInFiles_whenValidateImage_thenReturnFalse() {
    // given
    MultipartFile file1 = new MockMultipartFile(
        "test1",
        "test1.jpeg",
        String.valueOf(ContentType.IMAGE_JPEG),
        new byte[1024 * 1024]);

    MultipartFile file2 = new MockMultipartFile(
        "test2",
        "test2.txt",
        String.valueOf(ContentType.TEXT_PLAIN),
        new byte[4096 * 9192]);

    // when
    boolean result = FileUtil.validateImages(List.of(file1, file2));

    // then
    assertFalse(result);
  }
}