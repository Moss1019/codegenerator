package com.mossonthetree.codegenerator.service;

import com.mossonthetree.codegenerator.view.GuidGenerateRequest;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
public class GuidGeneratorService {
  private final Counter guidsGeneratedTotal;

  public GuidGeneratorService(MeterRegistry registry) {
    guidsGeneratedTotal = Counter.builder("codegen_guids_generated_total")
      .register(registry);
  }

  public List<String> generate(GuidGenerateRequest config) {
    List<String> uuids = new LinkedList<>();
    guidsGeneratedTotal.increment(config.getAmount());
    int numToGenerate = config.getAmount();
    numToGenerate = numToGenerate > 10 ? 5 : numToGenerate;
    for(int i = 0; i < numToGenerate; ++i) {
      String newId = UUID.randomUUID().toString();
      if(config.isBraced()) {
        newId = String.format("{%s}", newId);
      }
      if(!config.isDashed()) {
        newId = newId.replace("-", "");
      }
      uuids.add(newId);
    }
    return uuids;
  }

}
