package com.example.demo.service;

import com.amazonaws.services.s3.AmazonS3Client;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
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

      // 로그 파일을 찾아야 한다. 여러 개의 로그 파일중 일단 1개의 파일만 찾아본다.
      // 이미 s3에 기록된 로그 파일은 삭제해서 파일의 종복을 막는 방법도 있고,
      // aws cloud watch의 내용을 바로 s3에 전달하는 기능도 aws에서 제공한다고 한다.
      // 실제로 이 기능을 쓴다면, 이 작업은 task scheduler를 도입해야 하고 로그 파일들 중
      // 이미 작성이 끝나서 더 이상 수정할 필요가 없는 가장 오래된 파일을 s3에 업로드 해야할 듯 하다.
      List<String> lines = Files.readAllLines(Paths.get("./vr3_logs/vr3.log"));
      StringBuilder sb = new StringBuilder();
      for(String line : lines){
        sb.append(line);
        sb.append("\n");
      }

      sb.append("### logfile uploaded to s3 : ").append(LocalDateTime.now());

      amazonS3Client.putObject(
          bucketName, "test-log-file.log", sb.toString()
      );

      // 컨테이너에서는 로그를 삭제한다
      File uploadedLogFile = new File("./vr3_logs/vr3.log");
      boolean logfileDeleteResult = uploadedLogFile.delete();
      log.error("### uploadLogFile delete result : " + logfileDeleteResult);

    } catch (Exception e) {
      log.error("### uploadLogFile : exception occurred while uploading log file.");
    }

    return reuslt;
  }

}
