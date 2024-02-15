package com.palette.back_end.util.file;

import com.palette.back_end.util.exceptions.ErrorCode;
import com.palette.back_end.util.exceptions.PaletteException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


public class FileUtil {

  public static String generateFileName(MultipartFile file) {
    return UUID.randomUUID().toString().concat(getFileExtension(file.getOriginalFilename()));
  }

  public static boolean validateImages(List<MultipartFile> files) {
    for (MultipartFile file : files) {
      if (!validateSize(file) || !validateImageExtension(file)) {
        return false;
      }
    }

    return true;
  }

  private static boolean validateSize(MultipartFile file) {
    return ((file.getSize() / (1024 * 1024)) <= 20);
  }

  private static boolean validateImageExtension(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      return false;
    }

    String originalFilename = file.getOriginalFilename();
    if (originalFilename == null) {
      return false;
    }

    String extension = getFileExtension(originalFilename).toLowerCase();

    return (
        extension.equals(FileType.JPG.getExtension()) ||
        extension.equals(FileType.JPEG.getExtension()) ||
        extension.equals(FileType.PNG.getExtension()) ||
        extension.equals(FileType.SVG.getExtension()));
  }

  private static String getFileExtension(String fileName) {
    try {
      return fileName.substring(fileName.lastIndexOf('.'));
    }
    catch (StringIndexOutOfBoundsException e) {
      throw new PaletteException(ErrorCode.INVALID_FILE_EXTENSION);
    }
  }
}
