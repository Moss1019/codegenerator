package com.mossonthetree.codegenerator.service;

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
    SpringBootGenerator generator = new SpringBootGenerator(db, request);
    return generator.generate();
  }
}
