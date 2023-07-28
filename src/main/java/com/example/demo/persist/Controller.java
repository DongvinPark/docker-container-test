package com.example.demo.persist;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Controller {

  private final TestRepository testRepository;

  @PostMapping("/test")
  public TestEntity test(){
    return testRepository.save(
        TestEntity.builder()
            .name("test")
            .build()
    );
  }

}
