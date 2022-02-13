package com.mossonthetree.codegenerator.util;

import java.util.Map;
import java.util.HashMap;

public class TemplatePaths {
  private static Map<String, String> springBootTemplates;
  private static Map<String, String> djangoTemplates;
  
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

  public static Map<String, String> getDjangoTemplates() {
    if(djangoTemplates == null) {
      djangoTemplates = new HashMap<>();
      djangoTemplates.put("admin.py", "./templates/django/admin.tmpl");
      djangoTemplates.put("apps.py", "./templates/django/apps.tmpl");
      djangoTemplates.put("__init__.py", "./templates/django/init.tmpl");
    }
    return djangoTemplates;
  }
}
