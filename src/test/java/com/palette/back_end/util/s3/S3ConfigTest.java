package com.palette.back_end.util.s3;

import static org.junit.jupiter.api.Assertions.*;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;


public class S3ConfigTest {

  @Test
  public void testAmazonS3BeanCreation() {
    // given
    String expectedAccessKey = "testAccessKey";
    String expectedSecretKey = "testSecretKey";
    String expectedRegion = "testRegion";

    AWSCredentials expectedCredentials = new BasicAWSCredentials(expectedAccessKey, expectedSecretKey);

    S3Config s3Config = new S3Config();

    ReflectionTestUtils.setField(s3Config, "ACCESS_KEY", expectedAccessKey);
    ReflectionTestUtils.setField(s3Config, "SECRET_KEY", expectedSecretKey);
    ReflectionTestUtils.setField(s3Config, "REGION", expectedRegion);

    // when
    AmazonS3 amazonS3 = s3Config.amazonS3Client();

    // then
    assertNotNull(amazonS3);
    assert(amazonS3.getClass()).equals(AmazonS3Client.class);
  }
}
