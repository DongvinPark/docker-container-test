package com.example.demo.config;

import com.amazonaws.services.s3.AmazonS3Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class ShutdownEventListener implements ApplicationListener<ContextClosedEvent> {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3Client amazonS3Client;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        // 스프링부트 컨테이너가 종료될 때, 아직 S3로 올리지 않은 .txt 파일과 원본 .log파일을 전부 S3에 올린다.
        log.error("### onApplicationEvent : unexpected termination of application");

        File dir = new File("./vr3_logs");
        String[] fileNameArr = dir.list();

        assert fileNameArr != null;
        for(String logFileName : fileNameArr){
            if(logFileName.endsWith(".txt")){
                File s3UploadTargetFile = new File("./vr3_logs/" + logFileName);
                amazonS3Client.putObject(bucketName, logFileName, s3UploadTargetFile);
                s3UploadTargetFile.delete();
            } else {
                File lastLogFile = new File("./vr3_logs/vr3.log");

                LocalDateTime shutdownTime = LocalDateTime.now();

                String lastLogFileName = "vr3-"
                        + shutdownTime.getYear() + "-"
                        + shutdownTime.getMonthValue() + "-"
                        + shutdownTime.getDayOfMonth() + "-"
                        + shutdownTime.getHour() + "-"
                        + shutdownTime.getMinute() + ".terminated.txt";

                amazonS3Client.putObject(bucketName, lastLogFileName, lastLogFile);

                lastLogFile.delete();
            }
        }//for
    }
}
