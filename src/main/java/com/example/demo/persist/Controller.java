package com.example.demo.persist;

import com.example.demo.service.AmazonS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Controller {

  private final TestRepository testRepository;
  private final AmazonS3Service s3Service;

  @PostMapping("/test")
  public TestEntity test(){
    TestEntity testEntity =testRepository.save(
        TestEntity.builder()
            .name("test")
            .build()
    );
    log.info("### controller.test : TestEntity.entityId {} saved!", testEntity.getId());
    return testEntity;
  }

  @PostMapping("/log-test")
  public String logTest() {
    String result = s3Service.uploadLogFile();
    log.info("### controller.logTest : log file save at S3");
    return result;
  }

}
