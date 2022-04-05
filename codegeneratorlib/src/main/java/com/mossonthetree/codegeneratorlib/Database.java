package com.mossonthetree.codegeneratorlib;

import java.util.List;

public class Database {
    private final String rootName;
    private final String projectName;
    private final DatabaseType type;
    private final List<Table> tables;

    public Database(String rootName, String projectName, DatabaseType databaseType, List<Table> tables) {
        this.rootName = rootName;
        this.projectName = projectName;
        this.tables = tables;
        this.type = databaseType;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (Table table : tables) {
            b.append("\n");
            b.append(table.toString());
            b.append("\n---------------------------------");
        }
        return b.toString();
    }

    public String getProjectPath() {
        return String.format("%s.%s", rootName, projectName);
    }

    public String getRootName() {
        return rootName;
    }

    public String getProjectName() {
        return projectName;
    }

    public DatabaseType getDatabaseType() {
        return type;
    }

    public List<Table> getTables() {
        return tables;
    }
}
