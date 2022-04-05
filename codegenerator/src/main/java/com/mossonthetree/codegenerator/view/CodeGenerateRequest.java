package com.mossonthetree.codegenerator.view;

import com.mossonthetree.codegenerator.parameter.Environment;
import com.mossonthetree.codegenerator.parameter.ExternalSystem;
import com.mossonthetree.codegenerator.parameter.FrontendSystem;
import com.mossonthetree.codegeneratorlib.DatabaseType;

import java.io.Serializable;
import java.util.List;

public class CodeGenerateRequest implements Serializable {
    private String projectName;
    private String rootName;
    private String definition;
    private DatabaseType databaseType;
    private Environment environment;
    private List<ExternalSystem> externalSystems;
    private List<FrontendSystem> frontendSystems;

    public CodeGenerateRequest() {
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getRootName() {
        return rootName;
    }

    public void setRootName(String rootName) {
        this.rootName = rootName;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public List<ExternalSystem> getExternalSystems() {
        return externalSystems;
    }

    public void setExternalSystems(List<ExternalSystem> externalSystems) {
        this.externalSystems = externalSystems;
    }

    public List<FrontendSystem> getFrontendSystems() {
        return frontendSystems;
    }

    public void setFrontendSystems(List<FrontendSystem> frontendSystems) {
        this.frontendSystems = frontendSystems;
    }
}
