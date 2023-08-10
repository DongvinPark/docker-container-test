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

      // 스케줄링 스레드가 필요하다.
      // ./vr3_logs/라는 디렉토리에서 확장자가 .txt인 로그파일들을 전부 s3에 업로드 한다.
      // S3에 업로드 완료된 파일은 컨테이너 내부에서 삭제한다.
      File dir = new File("./vr3_logs");
      String[] fileNameArr = dir.list();

      for(String logFileName : fileNameArr){
        if(logFileName.endsWith(".txt")){
          File s3UploadTargetFile = new File("./vr3_logs/" + logFileName);

          amazonS3Client.putObject(bucketName, logFileName, s3UploadTargetFile);

          boolean deleteResult = s3UploadTargetFile.delete();

          log.info("### uploadLogFile delete result : " + deleteResult);
        }
      }//for

    } catch (Exception e) {
      log.error("### uploadLogFile : exception occurred while uploading log file.");
    }

    return reuslt;
  }

}
