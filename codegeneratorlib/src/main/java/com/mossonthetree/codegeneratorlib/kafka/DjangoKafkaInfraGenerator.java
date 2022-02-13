package com.mossonthetree.codegeneratorlib.kafka;

import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.Generator;
import com.mossonthetree.codegeneratorlib.Table;

import java.util.HashMap;
import java.util.Map;

public class DjangoKafkaInfraGenerator extends Generator {
    private String consumerTmpl;
    private String producerTmpl;

    public DjangoKafkaInfraGenerator(Database db) {
        super(db, "./templates/kafka/python");
    }

    @Override
    public Map<String, String> generate() {
        Map<String, String> files = new HashMap<>();
        for(Table t: db.getTables()) {
            files.put(t.getName() + "_producer.py", generateProducer(t));
            files.put(t.getName() + "_consumer.py", generateConsumer(t));
        }
        return files;
    }

    private String generateProducer(Table t) {
        return producerTmpl
                .replace("{tablepascal}", t.getPascalName())
                .replace("{tablename}", t.getName());
    }

    private String generateConsumer(Table t) {
        return consumerTmpl
                .replace("{tablepascal}", t.getPascalName())
                .replace("{tablename}", t.getName());
    }

    @Override
    protected void loadTemplates() {
        consumerTmpl = loadTemplate("consumer");
        producerTmpl = loadTemplate("producer");
    }
}
