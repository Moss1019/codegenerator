package com.mossonthetree.codegenerator.util;

import com.mossonthetree.codegeneratorlib.Database;

import java.util.HashMap;
import java.util.Map;

public class FilePathsGenerator {
  private static Map<String, String> springBootFilePaths;
  
  public static Map<String, String> getSpringBootFilePaths(Database db) {
    if(springBootFilePaths == null) {
      String rootAsPath = String.join("/", db.getRootName().split("\\."));
      springBootFilePaths = new HashMap<>();
      springBootFilePaths.put("application.properties", String.format("%s/src/main/resources", db.getProjectName()));
      springBootFilePaths.put(".gitignore", db.getProjectName());
      springBootFilePaths.put("mvnw", db.getProjectName());
      springBootFilePaths.put("mvnw.cmd", db.getProjectName());
      springBootFilePaths.put("pom.xml", db.getProjectName());
      springBootFilePaths.put("Program.java",
        String.format("%s/src/main/java/%s/%s", db.getProjectName(), rootAsPath, db.getProjectName()));
    }
    return springBootFilePaths;
  }
}
