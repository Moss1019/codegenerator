package com.mossonthetree.codegenerator.generators;

import com.mossonthetree.codegenerator.parameter.ExternalSystem;
import com.mossonthetree.codegenerator.parameter.FrontendSystem;
import com.mossonthetree.codegenerator.util.FilePathsGenerator;
import com.mossonthetree.codegenerator.util.TarFile;
import com.mossonthetree.codegenerator.util.TemplatePaths;
import com.mossonthetree.codegenerator.view.CodeGenerateRequest;
import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.FileHandler;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DjangoGenerator {
    private final Map<String, String> templatePaths = TemplatePaths.getDjangoTemplates();
    private final Map<String, String> generatedFilePaths;

    private final Database db;
    private final List<ExternalSystem> externalSystems;
    private final List<FrontendSystem> frontendSystems;
    private final TarFile tarFile;

    public DjangoGenerator(Database db, CodeGenerateRequest request) {
        this.db = db;
        generatedFilePaths = FilePathsGenerator.getDjangoFilePaths(db);
        externalSystems = request.getExternalSystems();
        frontendSystems = request.getFrontendSystems();
        tarFile = new TarFile();
    }

    public byte[] generate() {
        for (String fileName : generatedFilePaths.keySet()) {
            FileHandler fHandler = new FileHandler(templatePaths.get(fileName));
            String template = fHandler.readFile();
            String generatedFilePath = generatedFilePaths.get(fileName);
            String fullFileName = String.format("%s/%s", generatedFilePath, fileName);
            tarFile.addEntry(fullFileName, replaceTokens(template));
        }
        addBusinessLogicToTar();
        addFrontEndLogicToTar();
        byte[] data = tarFile.flush().getContent();
        tarFile.close();
        return data;
    }

    private String replaceTokens(String template) {
        String pascalProjectName = db.getProjectName().toUpperCase(Locale.ROOT).charAt(0) + db.getProjectName().substring(1);
        return template
                .replace("{projectnamepascal}", pascalProjectName)
                .replace("{projectname}", db.getProjectName());
    }

    private void addBusinessLogicToTar() {
        DjangoBusinessLogicGenerator djangoBusinessLogicGenerator = new DjangoBusinessLogicGenerator(db, externalSystems);
        Map<String, String> rootLevelFiles = djangoBusinessLogicGenerator.generateRootLevelFiles();
        for (String fileName : rootLevelFiles.keySet()) {
            String fullFileName = String.format("%s/%s", db.getProjectName(), fileName);
            String fileContent = rootLevelFiles.get(fileName);
            tarFile.addEntry(fullFileName, fileContent);
        }
        Map<String, Map<String, String>> folderFiles = djangoBusinessLogicGenerator.generate();
        for (String folderName : folderFiles.keySet()) {
            for (String fileName : folderFiles.get(folderName).keySet()) {
                String fullFileName = String.format("%s/%s/%s", db.getProjectName(), folderName, fileName);
                String fileContent = folderFiles.get(folderName).get(fileName);
                tarFile.addEntry(fullFileName, fileContent);
            }
        }
    }

    private void addFrontEndLogicToTar() {
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
