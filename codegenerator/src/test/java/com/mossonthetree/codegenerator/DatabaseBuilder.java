package com.mossonthetree.codegenerator;

import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.DatabaseType;
import com.mossonthetree.codegeneratorlib.Table;

import java.util.ArrayList;
import java.util.List;

public class DatabaseBuilder {
    private String rootName = "root";
    private String projectName = "project";
    private DatabaseType type = DatabaseType.Sql;
    private final List<Table> tables = new ArrayList<>();

    public DatabaseBuilder withRootName(String rootName) {
        this.rootName = rootName;
        return this;
    }

    public DatabaseBuilder withProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public DatabaseBuilder withType(DatabaseType type) {
        this.type = type;
        return this;
    }

    public DatabaseBuilder addTable(Table table) {
        tables.add(table);
        return this;
    }

    public Database build() {
        return new Database(rootName, projectName, type, tables);
    }
}
