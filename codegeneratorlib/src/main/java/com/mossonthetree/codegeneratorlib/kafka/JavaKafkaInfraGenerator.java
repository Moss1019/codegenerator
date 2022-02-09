package com.mossonthetree.codegeneratorlib.kafka;

import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.Generator;
import com.mossonthetree.codegeneratorlib.Table;

import java.util.HashMap;
import java.util.Map;

public class JavaKafkaInfraGenerator extends Generator {
  private String consumerTmpl;
  private String producerTmpl;
  private String serializerTmpl;
  private String deserializerTmpl;

  public JavaKafkaInfraGenerator(Database db) {
    super(db, "./templates/kafka/java");
  }


  @Override
  public Map<String, String> generate() {
    Map<String, String> files = new HashMap<>();
    for(Table t: db.getTables()) {
      files.put(t.getPascalName() + "ViewConsumer.java", generateConsumer(t));
      files.put(t.getPascalName() + "ViewProducer.java", generateProducer(t));
      files.put(t.getPascalName() + "ViewSerializer.java", generateSerializer(t));
      files.put(t.getPascalName() + "ViewDeserializer.java", generateDeserializer(t));
    }
    return files;
  }

  private String generateConsumer(Table t) {
    return consumerTmpl
      .replace("{rootname}", db.getProjectPath())
      .replace("{tablepascal}", t.getPascalName());
  }

  private String generateProducer(Table t) {
    return  producerTmpl
      .replace("{rootname}", db.getProjectPath())
      .replace("{tablepascal}", t.getPascalName())
      .replace("{tablecamel}", t.getCamelName());
  }

  private String generateSerializer(Table t) {
    return serializerTmpl
      .replace("{rootname}", db.getProjectPath())
      .replace("{tablepascal}", t.getPascalName())
      .replace("{tablecamel}", t.getCamelName());
  }

  private String generateDeserializer(Table t) {
    return deserializerTmpl
      .replace("{rootname}", db.getProjectPath())
      .replace("{tablepascal}", t.getPascalName());
  }

  @Override
  protected void loadTemplates() {
    consumerTmpl = loadTemplate("consumer");
    producerTmpl = loadTemplate("producer");
    serializerTmpl = loadTemplate("serializer");
    deserializerTmpl = loadTemplate("deserializer");
  }
}
