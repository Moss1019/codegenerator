package com.mossonthetree.codegenerator.generators;

import com.mossonthetree.codegenerator.parameter.ExternalSystem;
import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.elasticsearchdjango.DjangoElasticClientGenerator;
import com.mossonthetree.codegeneratorlib.kafka.DjangoKafkaInfraGenerator;
import com.mossonthetree.codegeneratorlib.python.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DjangoBusinessLogicGenerator {
    private final Database db;
    private final List<ExternalSystem> externalDependencies;

    public DjangoBusinessLogicGenerator(Database db, List<ExternalSystem> externalDependencies) {
        this.db = db;
        this.externalDependencies = externalDependencies;
    }

    public Map<String, Map<String, String>> generate() {
        Map<String, Map<String, String>> files = new HashMap<>();
        SqlDataStoreGenerator sqlDataStoreGenerator = new SqlDataStoreGenerator(db);
        Map<String, String> sqlDataFiles = sqlDataStoreGenerator.generate();
        sqlDataFiles.put("__init__.py", "");
        files.put("data_store", sqlDataFiles);
        ViewGenerator viewGenerator = new ViewGenerator(db);
        Map<String, String> viewFiles = viewGenerator.generate();
        viewFiles.put("__init__.py", "");
        files.put("views", viewGenerator.generate());
        for(ExternalSystem externalSystem: externalDependencies) {
            switch(externalSystem) {
                case elastic:
                    DjangoElasticClientGenerator djangoElasticClientGenerator = new DjangoElasticClientGenerator(db);
                    Map<String, String> elasticClientFiles = djangoElasticClientGenerator.generate();
                    elasticClientFiles.put("__init__.py", "");
                    files.put("elasticinfra", elasticClientFiles);
                    break;
                case kafka:
                    DjangoKafkaInfraGenerator kafkaGenerator = new DjangoKafkaInfraGenerator(db);
                    Map<String, String> kafkaFiles = kafkaGenerator.generate();
                    kafkaFiles.put("__init__.py", "");
                    files.put("kafkainfra", kafkaFiles);
                    break;
            }
        }
        return files;
    }

    public Map<String, String> generateRootLevelFiles() {
        Map<String, String> files = new HashMap<>();
        UrlGenerator urlGenerator = new UrlGenerator(db);
        Map<String, String> urlFiles = urlGenerator.generate();
        for(String fileName: urlFiles.keySet()) {
            files.put(fileName, urlFiles.get(fileName));
        }
        ModelsGenerator modelsGenerator = new ModelsGenerator(db);
        Map<String, String> modelFiles = modelsGenerator.generate();
        for(String fileName: modelFiles.keySet()) {
            files.put(fileName, modelFiles.get(fileName));
        }
        SerializerGenerator serializerGenerator = new SerializerGenerator(db);
        Map<String, String> serializerFiles = serializerGenerator.generate();
        for(String fileName: serializerFiles.keySet()) {
            files.put(fileName, serializerFiles.get(fileName));
        }
        return files;
    }
}
