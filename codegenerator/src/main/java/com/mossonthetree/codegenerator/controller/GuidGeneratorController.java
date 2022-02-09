package com.mossonthetree.codegenerator.controller;

import com.mossonthetree.codegenerator.view.GuidGenerateRequest;
import com.mossonthetree.codegenerator.service.GuidGeneratorService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("api/guids")
public class GuidGeneratorController {
  @Autowired
  private GuidGeneratorService service;

  private final Counter requestTotal;
  private final Timer requestDuration;

  public GuidGeneratorController(MeterRegistry registry) {
    requestTotal = Counter.builder("codegen_guids_requests_total")
      .register(registry);
    requestDuration = Timer.builder("codegen_guids_request_duration")
      .register(registry);
  }

  @PostMapping("")
  public ResponseEntity<?> generate(@RequestBody GuidGenerateRequest config) {
    requestTotal.increment();
    return requestDuration.record(() -> {
      return ResponseEntity.ok(service.generate(config));
    });
  }
}
