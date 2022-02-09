package com.mossonthetree.codegenerator;

import com.mossonthetree.codegenerator.parameter.Environment;
import com.mossonthetree.codegenerator.parameter.ExternalSystem;
import com.mossonthetree.codegenerator.parameter.FrontendSystem;
import com.mossonthetree.codegenerator.view.CodeGenerateRequest;
import com.mossonthetree.codegeneratorlib.DatabaseType;

import java.util.ArrayList;
import java.util.List;

public class CodeGenerateRequestBuilder {
  private String projectName = "example";
  private String rootName = "root";
  private String definition = "";
  private DatabaseType databaseType = DatabaseType.Sql;
  private Environment environment = Environment.java;
  private final List<ExternalSystem> externalSystems = new ArrayList<>();
  private final List<FrontendSystem> frontendSystems = new ArrayList<>();

  public CodeGenerateRequestBuilder withProjectName(String projectName) {
    this.projectName = projectName;
    return this;
  }

  public CodeGenerateRequestBuilder withRootName(String rootName) {
    this.rootName = rootName;
    return this;
  }

  public CodeGenerateRequestBuilder withDefinition(String definition) {
    this.definition = definition;
    return this;
  }

  public CodeGenerateRequestBuilder withDatabaseType(DatabaseType databaseType) {
    this.databaseType = databaseType;
    return this;
  }

  public CodeGenerateRequestBuilder withEnvironment(Environment environment) {
    this.environment = environment;
    return this;
  }

  public CodeGenerateRequestBuilder addExternalSystem(ExternalSystem externalSystem) {
    externalSystems.add(externalSystem);
    return this;
  }

  public CodeGenerateRequestBuilder addFrontendSystems(FrontendSystem frontendSystem) {
    frontendSystems.add(frontendSystem);
    return this;
  }

  public CodeGenerateRequest build() {
    CodeGenerateRequest request = new CodeGenerateRequest();
    request.setProjectName(projectName);
    request.setRootName(rootName);
    request.setDefinition(definition);
    request.setDatabaseType(databaseType);
    request.setEnvironment(environment);
    request.setExternalSystems(externalSystems);
    request.setFrontendSystems(frontendSystems);
    return request;
  }
}
