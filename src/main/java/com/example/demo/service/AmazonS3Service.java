package com.example.demo.service;

import com.amazonaws.services.s3.AmazonS3Client;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AmazonS3Service {

  @Value("${cloud.aws.s3.bucket}")
  private String bucketName;

  private final AmazonS3Client amazonS3Client;

  public String uploadLogFile() {

    String reuslt = "success";

    try {

      // 로그 파일을 찾아야 한다. 여러 개의 로그 파일중 일단 1개의 파일만 찾아보자.
      // 로그 파일을 찾는 로직은 지금과 같은 테스트 환경이 아니라 실제 배포 환경에서 구현한다.
      List<String> lines = Files.readAllLines(Paths.get("./vr3_logs/vr3.log"));
      StringBuilder sb = new StringBuilder();
      for(String line : lines){
        sb.append(line);
        sb.append("\n");
      }

      amazonS3Client.putObject(
          bucketName, "test-log-file", sb.toString()
      );
    } catch (Exception e) {
      log.error("### uploadLogFile : exception occurred while uploading log file.");
    }

    return reuslt;
  }

}
