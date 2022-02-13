package com.mossonthetree.codegenerator.util;

import com.mossonthetree.codegeneratorlib.Database;

import java.util.HashMap;
import java.util.Map;

public class FilePathsGenerator {
  private static Map<String, String> springBootFilePaths;
  private static Map<String, String> djangoFilePaths;
  
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

  public static Map<String, String> getDjangoFilePaths(Database db) {
    if(djangoFilePaths == null) {
      djangoFilePaths = new HashMap<>();
      djangoFilePaths.put("admin.py", db.getProjectName());
      djangoFilePaths.put("apps.py", db.getProjectName());
      djangoFilePaths.put("__init__.py", db.getProjectName());
    }
    return djangoFilePaths;
  }
}
