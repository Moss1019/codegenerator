package com.mossonthetree.codegenerator.generators;

import com.mossonthetree.codegenerator.parameter.FrontendSystem;
import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.flutter.DartHttpClientGenerator;
import com.mossonthetree.codegeneratorlib.flutter.DartModelGenerator;
import com.mossonthetree.codegeneratorlib.typescript.AxiosGenerator;
import com.mossonthetree.codegeneratorlib.typescript.TypeScriptModelGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrontendGenerator {
  private final Database db;

  public FrontendGenerator(Database db) {
    this.db = db;
  }

  public Map<String, Map<String, String>> generate(List<FrontendSystem> frontendSystems) {
    Map<String, Map<String, String>> files = new HashMap<>();
    for(FrontendSystem frontendSystem : frontendSystems) {
      switch (frontendSystem) {
        case react:
          AxiosGenerator axiosGenerator = new AxiosGenerator(db);
          files.put("axios", axiosGenerator.generate());
          TypeScriptModelGenerator tsModelGenerator = new TypeScriptModelGenerator(db);
          files.put("typescripmodels", tsModelGenerator.generate());
          break;
        case flutter:
          DartHttpClientGenerator httpClientGenerator = new DartHttpClientGenerator(db);
          files.put("flutterhttp", httpClientGenerator.generate());
          DartModelGenerator dartModelGenerator = new DartModelGenerator(db);
          files.put("fluttermodels", dartModelGenerator.generate());
          break;
      }
    }
    return files;
  }
}
