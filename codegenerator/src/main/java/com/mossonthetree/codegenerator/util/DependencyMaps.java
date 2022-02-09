package com.mossonthetree.codegenerator.util;

import java.util.HashMap;
import java.util.Map;

public class DependencyMaps {
  private static Map<String, DependencyInfo> springBootDependencies;
  
  public static Map<String, DependencyInfo> getSpringBootDependencies() {
    if(springBootDependencies == null) {
      springBootDependencies = new HashMap<>();
      springBootDependencies.put("elastic", new DependencyInfo("org.elasticsearch.client", "elasticsearch-rest-high-level-client"));
      springBootDependencies.put("kafka", new DependencyInfo("org.apache.kafka", "kafka-clients"));
      springBootDependencies.put("jacksonxml", new DependencyInfo("com.fasterxml.jackson.core", "jackson-databind"));
      springBootDependencies.put("springweb", new DependencyInfo("org.springframework.boot", "spring-boot-starter-web"));
      springBootDependencies.put("springjpa", new DependencyInfo("org.springframework.boot", "spring-boot-starter-data-jpa"));
      springBootDependencies.put("mysql", new DependencyInfo("mysql", "mysql-connector-java"));
    }
    return springBootDependencies;
  }
}
