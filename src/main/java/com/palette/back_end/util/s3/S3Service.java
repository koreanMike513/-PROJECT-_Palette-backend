package com.palette.back_end.util.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.palette.back_end.util.exceptions.ErrorCode;
import com.palette.back_end.util.exceptions.PaletteException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class S3Service {

  @Value("${spring.cloud.config.server.awss3.bucket}")
  private String BUCKET_NAME;

  private final AmazonS3 amazonS3;

  public String uploadImageToS3(MultipartFile file, String fileName) {
    ObjectMetadata metaData = new ObjectMetadata();
    metaData.setContentLength(file.getSize());
    metaData.setContentType(file.getContentType());

    try (InputStream inputStream = file.getInputStream()){
      amazonS3.putObject(new PutObjectRequest(BUCKET_NAME, fileName, inputStream, metaData));
      return amazonS3.getUrl(BUCKET_NAME, fileName).toString();
    }
    catch (IOException e) {
      throw new PaletteException(ErrorCode.FILE_CONVERSION);
    }
    catch (AmazonServiceException e) {
      throw new PaletteException(ErrorCode.AMAZON_S3_ERROR);
    }
  }

  public void deleteImageFromS3(String fileName) {
    try {
      amazonS3.deleteObject(BUCKET_NAME, fileName);
    }
    catch (AmazonServiceException e) {
      throw new PaletteException(ErrorCode.AMAZON_S3_ERROR);
    }
  }
}
