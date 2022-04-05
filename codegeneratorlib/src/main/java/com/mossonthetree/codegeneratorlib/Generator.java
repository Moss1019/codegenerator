package com.mossonthetree.codegeneratorlib;

import java.net.URL;
import java.util.Map;

public abstract class Generator {
    protected Database db;
    protected String baseTemplateDir;

    public Generator(Database db, String baseTemplateDir) {
        this.db = db;
        this.baseTemplateDir = baseTemplateDir;
        loadTemplates();
    }

    protected String loadTemplate(String fileName) {
        URL fileUrl = this.getClass().getResource(String.format("/%s/%s.tmpl", baseTemplateDir, fileName));
        FileHandler fh = new FileHandler(fileUrl);
        String content = fh.readFile();
        if (fh.isInError()) {
            System.err.println(this.getClass().getName());
            System.err.println("************************\nError while reading file " +
                    fileUrl +
                    "\n**************************");
        }
        return content;
    }

    public abstract Map<String, String> generate();

    protected abstract void loadTemplates();
}
