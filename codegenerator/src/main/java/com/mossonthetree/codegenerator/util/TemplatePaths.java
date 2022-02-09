package com.mossonthetree.codegenerator.util;

import java.util.HashMap;
import java.util.Map;

public class TemplatePaths {
  private static Map<String, String> springBootTemplates;
  
  public static Map<String, String> getSpringBootTemplates() {
    if(springBootTemplates == null) {
      springBootTemplates = new HashMap<>();
      springBootTemplates.put("application.properties", "./templates/springboot/applicationproperties.tmpl");
      springBootTemplates.put(".gitignore", "./templates/springboot/gitignore.tmpl");
      springBootTemplates.put("mvnw", "./templates/springboot/mvnwsh.tmpl");
      springBootTemplates.put("mvnw.cmd", "./templates/springboot/mvnwcmd.tmpl");
      springBootTemplates.put("pom.xml", "./templates/springboot/pom.tmpl");
      springBootTemplates.put("Program.java", "./templates/springboot/program.tmpl");
    }
    return springBootTemplates;
  }
}
