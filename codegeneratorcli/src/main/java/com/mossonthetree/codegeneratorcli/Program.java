package com.mossonthetree.codegeneratorcli;

import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.DatabaseType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Program {
  public static void main(String[] args) {
    String root = "todo";
    String projectName = "coffeeshop";
    DatabaseType dbType = DatabaseType.InMemory;
    try {
      Database db = (new DatabaseFactory()).create(loadTestFile(), projectName, root, dbType);
      System.out.println(db);

      generateDjangoFiles(db);

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private static void generateDjangoFiles(Database db) {
    com.mossonthetree.codegeneratorlib.python.ModelsGenerator modelsGenerator = new com.mossonthetree.codegeneratorlib.python.ModelsGenerator(db);
    writeFiles(modelsGenerator.generate(), "");

    com.mossonthetree.codegeneratorlib.python.SerializerGenerator serializerGenerator = new com.mossonthetree.codegeneratorlib.python.SerializerGenerator(db);
    writeFiles(serializerGenerator.generate(), "");

    com.mossonthetree.codegeneratorlib.python.SqlDataStoreGenerator sqlDataStoreGenerator = new com.mossonthetree.codegeneratorlib.python.SqlDataStoreGenerator(db);
    writeFiles(sqlDataStoreGenerator.generate(), "data_store");

    com.mossonthetree.codegeneratorlib.python.UrlGenerator urlGenerator = new com.mossonthetree.codegeneratorlib.python.UrlGenerator(db);
    writeFiles(urlGenerator.generate(), "");

    com.mossonthetree.codegeneratorlib.python.ViewGenerator viewGenerator = new com.mossonthetree.codegeneratorlib.python.ViewGenerator(db);
    writeFiles(viewGenerator.generate(), "views");

    com.mossonthetree.codegeneratorlib.elasticsearchdjango.DjangoElasticClientGenerator elasticGenerator = new com.mossonthetree.codegeneratorlib.elasticsearchdjango.DjangoElasticClientGenerator(db);
    writeFiles(elasticGenerator.generate(), "elasticinfra");
  }

  private static void generateJavaElasticInfra(Database db) {
    com.mossonthetree.codegeneratorlib.elasticsearch.JavaElasticClientGenerator javaElasticClientGenerator = new com.mossonthetree.codegeneratorlib.elasticsearch.JavaElasticClientGenerator(db);
    writeFiles(javaElasticClientGenerator.generate(), "elasticinfra");
  }

  private static void generateJavaKafkaInfra(Database db) {
    com.mossonthetree.codegeneratorlib.kafka.JavaKafkaInfraGenerator javaInfraGenerator = new com.mossonthetree.codegeneratorlib.kafka.JavaKafkaInfraGenerator(db);
    writeFiles(javaInfraGenerator.generate(), "kafkainfra");
  }

  private static void generateFlutter(Database db) {
    com.mossonthetree.codegeneratorlib.flutter.DartHttpClientGenerator httpClientGenerator = new com.mossonthetree.codegeneratorlib.flutter.DartHttpClientGenerator(db);
    writeFiles(httpClientGenerator.generate(), "services");

    com.mossonthetree.codegeneratorlib.flutter.DartModelGenerator modelGenerator = new com.mossonthetree.codegeneratorlib.flutter.DartModelGenerator(db);
    writeFiles(modelGenerator.generate(), "models");
  }

  private static void generateAxiosHttp(Database db) {
    com.mossonthetree.codegeneratorlib.typescript.AxiosGenerator axiosGenerator = new com.mossonthetree.codegeneratorlib.typescript.AxiosGenerator(db);
    writeFiles(axiosGenerator.generate(), "http");

    com.mossonthetree.codegeneratorlib.typescript.TypeScriptModelGenerator modelGenerator = new com.mossonthetree.codegeneratorlib.typescript.TypeScriptModelGenerator(db);
    writeFiles(modelGenerator.generate(), "models");
  }

  private static void generateCshFiles(Database db) {
    com.mossonthetree.codegeneratorlib.asp.ControllerGenerator controllerGenerator = new com.mossonthetree.codegeneratorlib.asp.ControllerGenerator(db);
    writeFiles(controllerGenerator.generate(), "controllers");

    com.mossonthetree.codegeneratorlib.asp.EntityGenerator entityGenerator = new com.mossonthetree.codegeneratorlib.asp.EntityGenerator(db);
    writeFiles(entityGenerator.generate(), "entities");

    com.mossonthetree.codegeneratorlib.asp.InMemoryRepositoryGenerator inMemoryRepositoryGenerator = new com.mossonthetree.codegeneratorlib.asp.InMemoryRepositoryGenerator(db);
    writeFiles(inMemoryRepositoryGenerator.generate(), "inmemrepos");

    com.mossonthetree.codegeneratorlib.asp.MapperGenerator mapperGenerator = new com.mossonthetree.codegeneratorlib.asp.MapperGenerator(db);
    writeFiles(mapperGenerator.generate(), "mappers");

    com.mossonthetree.codegeneratorlib.asp.ServiceGenerator serviceGenerator = new com.mossonthetree.codegeneratorlib.asp.ServiceGenerator(db);
    writeFiles(serviceGenerator.generate(), "services");

    com.mossonthetree.codegeneratorlib.asp.SqlRepositoryGenerator sqlRepositoryGenerator = new com.mossonthetree.codegeneratorlib.asp.SqlRepositoryGenerator(db);
    writeFiles(sqlRepositoryGenerator.generate(), "sqlrepos");

    com.mossonthetree.codegeneratorlib.asp.ViewGenerator viewGenerator = new com.mossonthetree.codegeneratorlib.asp.ViewGenerator(db);
    writeFiles(viewGenerator.generate(), "views");
  }

  private static void generateJavaFiles(Database db) {
    com.mossonthetree.codegeneratorlib.java.ControllerGenerator controllerGenerator = new com.mossonthetree.codegeneratorlib.java.ControllerGenerator(db);
    writeFiles(controllerGenerator.generate(), "controller");

    com.mossonthetree.codegeneratorlib.java.EntityGenerator entityGenerator = new com.mossonthetree.codegeneratorlib.java.EntityGenerator(db);
    writeFiles(entityGenerator.generate(), "entity");

    com.mossonthetree.codegeneratorlib.java.InMemoryRepositoryGenerator inMemoryRepositoryGenerator = new com.mossonthetree.codegeneratorlib.java.InMemoryRepositoryGenerator(db);
    writeFiles(inMemoryRepositoryGenerator.generate(), "inmmerepo");

    com.mossonthetree.codegeneratorlib.java.JpaEntityGenerator jpaEntityGenerator = new com.mossonthetree.codegeneratorlib.java.JpaEntityGenerator(db);
    writeFiles(jpaEntityGenerator.generate(), "jpaentity");

    com.mossonthetree.codegeneratorlib.java.JpaMapperGenerator jpaMapperGenerator = new com.mossonthetree.codegeneratorlib.java.JpaMapperGenerator(db);
    writeFiles(jpaMapperGenerator.generate(), "jpamapper");

    com.mossonthetree.codegeneratorlib.java.JpaRepositoryGenerator jpaRepositoryGenerator = new com.mossonthetree.codegeneratorlib.java.JpaRepositoryGenerator(db);
    writeFiles(jpaRepositoryGenerator.generate(), "jparepo");

    com.mossonthetree.codegeneratorlib.java.MapperGenerator mapperGenerator = new com.mossonthetree.codegeneratorlib.java.MapperGenerator(db);
    writeFiles(mapperGenerator.generate(), "mapper");

    com.mossonthetree.codegeneratorlib.java.ServiceGenerator serviceGenerator = new com.mossonthetree.codegeneratorlib.java.ServiceGenerator(db);
    writeFiles(serviceGenerator.generate(), "service");

    com.mossonthetree.codegeneratorlib.java.ViewGenerator viewGenerator = new com.mossonthetree.codegeneratorlib.java.ViewGenerator(db);
    writeFiles(viewGenerator.generate(), "view");
  }

  private static String loadTestFile() {
    System.out.println(System.getProperty("user.dir"));
    String fileName = "test.txt";
    File f = new File(fileName);
    if(f.exists()) {
      try (FileInputStream fIn = new FileInputStream(f)) {
        byte[] bytes = fIn.readAllBytes();
        return new String(bytes);
      } catch (Exception ignored) {}
    }
    return "";
  }

  private static void writeFiles(Map<String, String> files, String outputDir) {
    for(String key: files.keySet()) {
      String content = files.get(key);
      String path = String.format("./output/%s", outputDir);
      File d = new File(path);
      if(!d.exists()) {
        d.mkdirs();
      }
      File f = new File(path, key);
      try (FileOutputStream fOut = new FileOutputStream(f)) {
        fOut.write(content.getBytes(StandardCharsets.UTF_8));
      } catch (Exception ignored) {}
    }
  }
}
