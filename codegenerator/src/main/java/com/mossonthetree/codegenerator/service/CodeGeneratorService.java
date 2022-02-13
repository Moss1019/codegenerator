package com.mossonthetree.codegenerator.service;

import com.mossonthetree.codegenerator.generators.DjangoGenerator;
import com.mossonthetree.codegenerator.generators.SpringBootGenerator;
import com.mossonthetree.codegenerator.util.DatabaseFactory;
import com.mossonthetree.codegenerator.view.CodeGenerateRequest;
import com.mossonthetree.codegeneratorlib.Database;
import org.springframework.stereotype.Service;

@Service
public class CodeGeneratorService {
  public byte[] generate(CodeGenerateRequest request) {
    Database db = new DatabaseFactory().createDatabase(request);
    if(db == null) {
      return null;
    }
    switch (request.getEnvironment()) {
      case java: {
        SpringBootGenerator generator = new SpringBootGenerator(db, request);
        return generator.generate();
      }
      case django: {
        DjangoGenerator generator = new DjangoGenerator(db, request);
        return generator.generate();
      }
    }
    return null;
  }
}
