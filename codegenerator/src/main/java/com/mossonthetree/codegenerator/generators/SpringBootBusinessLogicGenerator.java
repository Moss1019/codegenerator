package com.mossonthetree.codegenerator.generators;

import com.mossonthetree.codegenerator.parameter.ExternalSystem;
import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.elasticsearch.JavaElasticClientGenerator;
import com.mossonthetree.codegeneratorlib.java.*;
import com.mossonthetree.codegeneratorlib.kafka.JavaKafkaInfraGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpringBootBusinessLogicGenerator {
  private final Database db;
  private final List<ExternalSystem> externalSystems;

  public SpringBootBusinessLogicGenerator(Database db, List<ExternalSystem> externalSystems) {
    this.db = db;
    this.externalSystems = externalSystems;
  }

  public  Map<String, Map<String, String>> generate() {
    Map<String, Map<String, String>> files = new HashMap<>();

    ControllerGenerator controllerGenerator = new ControllerGenerator(db);
    files.put("controller", controllerGenerator.generate());

    ServiceGenerator serviceGenerator = new ServiceGenerator(db);
    files.put("service", serviceGenerator.generate());

    ViewGenerator viewGenerator = new ViewGenerator(db);
    files.put("view", viewGenerator.generate());

    switch (db.getDatabaseType()) {
      case Document:
      case InMemory:
        MapperGenerator mapperGenerator = new MapperGenerator(db);
        files.put("mapper", mapperGenerator.generate());

        EntityGenerator entityGenerator = new EntityGenerator(db);
        files.put("entity", entityGenerator.generate());

        InMemoryRepositoryGenerator inMemoryRepositoryGenerator = new InMemoryRepositoryGenerator(db);
        files.put("repository", inMemoryRepositoryGenerator.generate());
        break;
      case Sql:
        JpaMapperGenerator jpaMapperGenerator = new JpaMapperGenerator(db);
        files.put("mapper", jpaMapperGenerator.generate());

        JpaEntityGenerator jpaEntityGenerator = new JpaEntityGenerator(db);
        files.put("entity", jpaEntityGenerator.generate());

        JpaRepositoryGenerator jpaRepositoryGenerator = new JpaRepositoryGenerator(db);
        files.put("repository", jpaRepositoryGenerator.generate());
        break;
    }

    for (ExternalSystem externalSystem : externalSystems) {
      switch (externalSystem) {
        case elastic:
          JavaElasticClientGenerator javaElasticClientGenerator = new JavaElasticClientGenerator(db);
          files.put("elasticinfra", javaElasticClientGenerator.generate());
          break;
        case kafka:
          JavaKafkaInfraGenerator javaKafkaInfraGenerator = new JavaKafkaInfraGenerator(db);
          files.put("kafkainfra", javaKafkaInfraGenerator.generate());
          break;
      }
    }
    return files;
  }
}
