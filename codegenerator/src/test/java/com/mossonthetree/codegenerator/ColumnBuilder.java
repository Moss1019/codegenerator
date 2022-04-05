package com.mossonthetree.codegenerator;

import com.mossonthetree.codegeneratorlib.Column;

public class ColumnBuilder {
    private String name = "column";
    private String dataType = "int";
    private String foreignTableName = "";
    private boolean primary = false;
    private boolean secondary = false;
    private boolean autoIncrement = false;
    private boolean foreign = false;
    private boolean unique = false;

    public ColumnBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ColumnBuilder withDataType(String dataType) {
        this.dataType = dataType;
        return this;
    }

    public ColumnBuilder withForeignTableName(String foreignTableName) {
        this.foreignTableName = foreignTableName;
        return this;
    }

    public ColumnBuilder asPrimary() {
        primary = true;
        secondary = false;
        return this;
    }

    public ColumnBuilder asSecondary() {
        primary = false;
        secondary = true;
        return this;
    }

    public ColumnBuilder asAutoIncrement() {
        autoIncrement = true;
        return this;
    }

    public ColumnBuilder asForeign() {
        foreign = true;
        return this;
    }

    public ColumnBuilder asUnique() {
        unique = true;
        return this;
    }

    public Column build() {
        return new Column(name, dataType, foreignTableName, primary, secondary, autoIncrement, foreign, unique);
    }
}
