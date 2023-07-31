package com.example.demo.persist;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Controller {

  private final TestRepository testRepository;

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

}
