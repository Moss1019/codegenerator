package com.mossonthetree.codegenerator;

import com.mossonthetree.codegeneratorlib.Column;
import com.mossonthetree.codegeneratorlib.Table;

import java.util.ArrayList;
import java.util.List;

public class TableBuilder {
    private String name = "table";
    private String loopedJoinedName = "";
    private boolean joined = false;
    private boolean looped = false;
    private final List<Column> columns = new ArrayList<>();

    public TableBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TableBuilder withLoopedJoinedName(String loopedJoinedName) {
        this.loopedJoinedName = loopedJoinedName;
        return this;
    }

    public TableBuilder asJoined() {
        joined = true;
        return this;
    }

    public TableBuilder asLooped() {
        looped = true;
        return this;
    }

    public TableBuilder addColumn(Column column) {
        columns.add(column);
        return this;
    }

    public Table build() {
        return new Table(name, joined, looped, loopedJoinedName, columns);
    }
}
