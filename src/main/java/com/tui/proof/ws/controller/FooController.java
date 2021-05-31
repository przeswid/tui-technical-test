package com.tui.proof.ws.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class FooController {

  @GetMapping("/")
  void test() {
    log.info("Foo controller");
  }
}
