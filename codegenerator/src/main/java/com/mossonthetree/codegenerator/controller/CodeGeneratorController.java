package com.mossonthetree.codegenerator.controller;

import com.mossonthetree.codegenerator.service.CodeGeneratorService;
import com.mossonthetree.codegenerator.view.CodeGenerateRequest;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Base64;

@Controller
@RequestMapping("api/generated-apps")
public class CodeGeneratorController {
  @Autowired
  private CodeGeneratorService service;

  private final Counter requestTotals;
  private final Timer requestDuration;

  public CodeGeneratorController(MeterRegistry registry) {
    requestTotals = Counter.builder("codegen_generated_apps_requests_total")
      .register(registry);
    requestDuration = Timer.builder("codegen_generated_apps_request_duration")
      .register(registry);
  }

  @PostMapping("")
  public ResponseEntity<?> generate(@RequestBody CodeGenerateRequest request) {
    byte[] gzipFile = service.generate(request);
    requestTotals.increment();
    return requestDuration.record(() -> {
      String encodedString = Base64.getEncoder().encodeToString(gzipFile);
      return ResponseEntity.ok(encodedString);
    });
  }
}
