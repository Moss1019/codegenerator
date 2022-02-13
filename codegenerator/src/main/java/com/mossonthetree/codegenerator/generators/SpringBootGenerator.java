package com.mossonthetree.codegenerator.generators;

import com.mossonthetree.codegenerator.parameter.ExternalSystem;
import com.mossonthetree.codegenerator.parameter.FrontendSystem;
import com.mossonthetree.codegenerator.util.*;
import com.mossonthetree.codegenerator.view.CodeGenerateRequest;
import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.FileHandler;

import java.util.List;
import java.util.Map;

public class SpringBootGenerator {
    private final Map<String, DependencyInfo> dependencyInfoMap = DependencyMaps.getSpringBootDependencies();
    private final Map<String, String> templatePaths = TemplatePaths.getSpringBootTemplates();
    private final Map<String, String> generatedFilePaths;

    private final Database db;
    private final List<ExternalSystem> externalSystems;
    private final List<FrontendSystem> frontendSystems;
    private final String dependencyTmpl;
    private final String mysqlConfigTmpl;
    private final TarFile tarFile;
    private final String rootAsPath;

    public SpringBootGenerator(Database db, CodeGenerateRequest request) {
        this.db = db;
        rootAsPath = String.join("/", db.getRootName().split("\\."));
        generatedFilePaths = FilePathsGenerator.getSpringBootFilePaths(db);
        externalSystems = request.getExternalSystems();
        frontendSystems = request.getFrontendSystems();
        dependencyTmpl = new FileHandler("./templates/springboot/dependency.tmpl").readFile();
        mysqlConfigTmpl = new FileHandler("./templates/springboot/mysqlconfig.tmpl").readFile();
        tarFile = new TarFile();
    }

    public byte[] generate() {
        for (String fileName : generatedFilePaths.keySet()) {
            FileHandler fHandler = new FileHandler(templatePaths.get(fileName));
            String template = fHandler.readFile();
            String generatedFilePath = generatedFilePaths.get(fileName);
            tarFile.addEntry(String.format("%s/%s", generatedFilePath, fileName), replaceTokens(template));
        }
        addBusinessLogicToTar();
        addFrontendLogicToTar();
        byte[] data = tarFile.flush().getContent();
        tarFile.close();
        return data;
    }

    private String replaceTokens(String template) {
        return template
                .replace("{springbootstarterparentversion}", getSpringParentVersion())
                .replace("{rootname}", db.getRootName())
                .replace("{projectname}", db.getProjectName())
                .replace("{dependencies}", generateDependencies())
                .replace("{javaversion}", "17")
                .replace("{dbconfig}", generateDBConfig());
    }

    private String getSpringParentVersion() {
        PomDependencyVersionResolver resolver = new PomDependencyVersionResolver();
        return resolver.resolveVersion(new DependencyInfo("org.springframework.boot", "spring-boot-starter-parent"));
    }

    private String generateDependencies() {
        boolean needsJackson = false;
        PomDependencyVersionResolver versionResolver = new PomDependencyVersionResolver();
        StringBuilder b = new StringBuilder();
        DependencyInfo webInfo = dependencyInfoMap.get("springweb");
        b
                .append("\n")
                .append(dependencyTmpl
                        .replace("{groupid}", webInfo.getGroupId())
                        .replace("{artifactid}", webInfo.getArtifactId())
                        .replace("{version}", versionResolver.resolveVersion(webInfo)));
        for (ExternalSystem externalSystem : externalSystems) {
            DependencyInfo info = null;
            switch (externalSystem) {
                case elastic:
                    info = dependencyInfoMap.get("elastic");
                    needsJackson = true;
                    break;
                case kafka:
                    info = dependencyInfoMap.get("kafka");
                    needsJackson = true;
                    break;
            }
            b
                    .append("\n")
                    .append(dependencyTmpl
                            .replace("{groupid}", info.getGroupId())
                            .replace("{artifactid}", info.getArtifactId())
                            .replace("{version}", versionResolver.resolveVersion(info)));
        }
        switch (db.getDatabaseType()) {
            case Sql:
                needsJackson = true;
                DependencyInfo jpaInfo = dependencyInfoMap.get("springjpa");
                DependencyInfo mysqlInfo = dependencyInfoMap.get("mysql");
                b
                        .append("\n")
                        .append(dependencyTmpl
                                .replace("{groupid}", jpaInfo.getGroupId())
                                .replace("{artifactid}", jpaInfo.getArtifactId())
                                .replace("{version}", versionResolver.resolveVersion(jpaInfo)))
                        .append("\n")
                        .append(dependencyTmpl
                                .replace("{groupid}", mysqlInfo.getGroupId())
                                .replace("{artifactid}", mysqlInfo.getArtifactId())
                                .replace("{version}", versionResolver.resolveVersion(mysqlInfo)));
            case Document:
            case InMemory:
            default:
                break;
        }
        if (needsJackson) {
            DependencyInfo jacksonInfo = dependencyInfoMap.get("jacksonxml");
            b
                    .append("\n")
                    .append(dependencyTmpl
                            .replace("{groupid}", jacksonInfo.getGroupId())
                            .replace("{artifactid}", jacksonInfo.getArtifactId())
                            .replace("{version}", versionResolver.resolveVersion(jacksonInfo)));
        }
        return b.toString();
    }

    private String generateDBConfig() {
        StringBuilder b = new StringBuilder();
        switch (db.getDatabaseType()) {
            case Sql:
                b
                        .append("\n")
                        .append(mysqlConfigTmpl
                                .replace("{appname}", db.getProjectName()));
                break;
            case Document:
            case InMemory:
            default:
                break;
        }
        return b.toString();
    }

    private void addBusinessLogicToTar() {
        SpringBootBusinessLogicGenerator springBootBusinessLogicGenerator = new SpringBootBusinessLogicGenerator(db, externalSystems);
        Map<String, Map<String, String>> files = springBootBusinessLogicGenerator.generate();
        for (String packageName : files.keySet()) {
            for (String fileName : files.get(packageName).keySet()) {
                String fileContent = files.get(packageName).get(fileName);
                String fullFileName = String.format("%s/src/main/java/%s/%s/%s/%s",
                        db.getProjectName(), rootAsPath, db.getProjectName(), packageName, fileName);
                tarFile.addEntry(fullFileName, fileContent);
            }
        }
    }

    private void addFrontendLogicToTar() {
        FrontendGenerator frontendGenerator = new FrontendGenerator(db);
        Map<String, Map<String, String>> files = frontendGenerator.generate(frontendSystems);
        for (String folderName : files.keySet()) {
            for (String fileName : files.get(folderName).keySet()) {
                String fileContent = files.get(folderName).get(fileName);
                String fullFileName = String.format("%s/%s", folderName, fileName);
                tarFile.addEntry(fullFileName, fileContent);
            }
        }
    }
}
