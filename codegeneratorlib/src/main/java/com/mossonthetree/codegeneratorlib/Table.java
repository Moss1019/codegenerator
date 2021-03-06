package com.mossonthetree.codegeneratorlib;

import java.util.*;

public class Table {
    private static final Map<String, Table> tableMapping = new HashMap<>();

    private final String name;
    private final List<Column> columns;
    private final boolean joined;
    private final boolean looped;
    private final String loopedJoinedName;

    private Column primaryColumn;
    private Column secondaryColumn;
    private List<Column> uniqueColumns;
    private List<Column> nonPrimaryColumns;
    private List<Table> parentTables;
    private List<Table> childTables;
    private List<Table> nonLoopedNonJoinedTables;
    private String pascalName;
    private String camelName;
    private String loopedJoinedPascal;
    private String loopedJoinedCamel;
    private boolean lists;

    public Table(String name, boolean joined, boolean looped, String loopedJoinedName, List<Column> columns) {
        this.name = name;
        this.joined = joined;
        this.looped = looped;
        this.loopedJoinedName = loopedJoinedName;
        this.columns = columns;
        setupNames();
        setupColumns();

        tableMapping.put(name, this);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(name);
        b.append(looped ? " looped" : joined ? " joined" : " =>");
        for (Column col : columns) {
            b.append("\n");
            b.append(col.toString());
        }
        return b.toString();
    }

    public String getName() {
        return name;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public boolean isJoined() {
        return joined;
    }

    public boolean isLooped() {
        return looped;
    }

    public Column getPrimaryColumn() {
        return primaryColumn;
    }

    public Column getSecondaryColumn() {
        if (secondaryColumn == null) {
            return primaryColumn;
        }
        return secondaryColumn;
    }

    public List<Column> getUniqueColumns() {
        return uniqueColumns;
    }

    public List<Column> getNonPrimaryColumns() {
        return nonPrimaryColumns;
    }

    public List<Table> getParentTables() {
        return parentTables;
    }

    public List<Table> getChildTables() {
        return childTables;
    }

    public List<Table> getNonJoinedNonLoopedTables() {
        if (nonLoopedNonJoinedTables == null) {
            nonLoopedNonJoinedTables = new ArrayList<>();
            for (Table ct : childTables) {
                if (ct.isJoined() || ct.isLooped()) {
                    continue;
                }
                nonLoopedNonJoinedTables.add(ct);
            }
        }
        return nonLoopedNonJoinedTables;
    }

    public String getPascalName() {
        return pascalName;
    }

    public String getCamelName() {
        return camelName;
    }

    public String getLowerName() {
        return pascalName.toLowerCase();
    }

    public String getLoopedJoinedName() {
        return loopedJoinedName;
    }

    public String getLoopedJoinedPascal() {
        return loopedJoinedPascal;
    }

    public String getLoopedJoinedCamel() {
        return loopedJoinedCamel;
    }

    public String getLoopedJoinedLower() {
        return loopedJoinedCamel.toLowerCase();
    }

    public boolean hasLists() {
        return lists;
    }

    private void setupColumns() {
        uniqueColumns = new ArrayList<>();
        nonPrimaryColumns = new ArrayList<>();
        parentTables = new ArrayList<>();
        childTables = new ArrayList<>();
        lists = false;
        for (Column col : columns) {
            col.setOwnerTable(this);
            if (col.isPrimary()) {
                primaryColumn = col;
            } else if (col.isSecondary()) {
                secondaryColumn = col;
            } else {
                nonPrimaryColumns.add(col);
            }
            if (col.isUnique()) {
                uniqueColumns.add(col);
            }
            if (col.isForeign()) {
                Table foreignTable = tableMapping.get(col.getForeignTableName());
                parentTables.add(foreignTable);
                col.setForeignTable(foreignTable);
                foreignTable.lists = true;
                if (!foreignTable.getChildTables().contains(this)) {
                    foreignTable.getChildTables().add(this);
                }
            }
        }
    }

    private void setupNames() {
        String[] parts = name.split("_");
        StringBuilder c = new StringBuilder();
        StringBuilder p = new StringBuilder();
        for (int i = 0; i < parts.length; ++i) {
            String part = parts[i];
            p.append(part.toUpperCase().charAt(0));
            p.append(part.substring(1).toLowerCase());
            if (i == 0) {
                c.append(part.toLowerCase().charAt(0));
            } else {
                c.append(part.toUpperCase().charAt(0));
            }
            c.append(part.substring(1).toLowerCase());
        }
        pascalName = p.toString();
        camelName = c.toString();
        if (loopedJoinedName.length() > 0) {
            StringBuilder lp = new StringBuilder();
            StringBuilder lc = new StringBuilder();
            String[] loopedJoinedParts = loopedJoinedName.split("_");
            for (int i = 0; i < loopedJoinedParts.length; ++i) {
                String part = loopedJoinedParts[i];
                lp.append(part.toUpperCase().charAt(0));
                lp.append(part.toLowerCase().substring(1));
                if (i == 0) {
                    lc.append(part.toLowerCase().charAt(0));
                } else {
                    lc.append(part.toUpperCase().charAt(0));
                }
                lc.append(part.toLowerCase().substring(1));
            }
            loopedJoinedPascal = lp.toString();
            loopedJoinedCamel = lc.toString();
        } else {
            loopedJoinedPascal = "";
            loopedJoinedCamel = "";
        }
    }
}
