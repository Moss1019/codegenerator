package com.mossonthetree.codegeneratorlib.java;

import com.mossonthetree.codegeneratorlib.*;

import java.util.HashMap;
import java.util.Map;

public class EntityGenerator extends Generator {
    private String classTmpl;
    private String childListTmpl;
    private String fieldTmpl;
    private String getterTmpl;
    private String setterTmpl;
    private String implementationTmpl;
    private String joiningImplementationTmpl;
    private String repoObjTmpl;
    private String joinedRepoObjTmpl;

    public EntityGenerator(Database db) {
        super(db, "./templates/java/entities");
    }

    @Override
    public Map<String, String> generate() {
        Map<String, String> files = new HashMap<>();
        files.put("RepoObj.java", repoObjTmpl.replace("{rootname}", db.getProjectPath()));
        files.put("JoinedRepoObj.java", joinedRepoObjTmpl.replace("{rootname}", db.getProjectPath()));
        for (Table t : db.getTables()) {
            files.put(t.getPascalName() + ".java", generateEntity(t));
        }
        return files;
    }

    private String generateEntity(Table t) {
        String implement = db.getDatabaseType() == DatabaseType.InMemory ?
                t.isJoined() || t.isLooped() ? "implements JoinedRepoObj<{primarydatatype}, {secondarydatatype}> "
                        : "implements RepoObj<{primarydatatype}> "
                : "";
        String implementation = db.getDatabaseType() == DatabaseType.InMemory ?
                t.isJoined() || t.isLooped() ? joiningImplementationTmpl.replace("{primarycamel}", t.getPrimaryColumn().getCamelName()).replace("{secondarycamel}", t.getSecondaryColumn().getCamelName())
                        : implementationTmpl.replace("{primarycamel}", t.getPrimaryColumn().getCamelName())
                : "";

        return classTmpl
                .replace("{fields}", generateFields(t))
                .replace("{childlists}", generateChildLists(t))
                .replace("{getters}", generateGetters(t))
                .replace("{setters}", generateSetters(t))
                .replace("{tablepascal}", t.getPascalName())
                .replace("{rootname}", db.getProjectPath())
                .replace("{implement}", implement)
                .replace("{implementation}", implementation)
                .replace("{primarydatatype}", DataTypeUtil.resolveWrapper(t.getPrimaryColumn()))
                .replace("{secondarydatatype}", DataTypeUtil.resolveWrapper(t.getSecondaryColumn()));
    }

    private String generateFields(Table t) {
        StringBuilder b = new StringBuilder();
        int i = 0;
        for (Column c : t.getColumns()) {
            b.append(fieldTmpl
                    .replace("{datatype}", DataTypeUtil.resolvePrimitive(c))
                    .replace("{columncamel}", c.getCamelName())
                    .replace("{default}", DataTypeUtil.resolveDefault(c)));
            if (++i < t.getColumns().size()) {
                b.append("\n");
            }
        }
        return b.toString();
    }

    private String generateChildLists(Table t) {
        StringBuilder b = new StringBuilder();
        int i = 0;
        for (Table ct : t.getChildTables()) {
            if (ct.isLooped() || ct.isJoined()) {
                ++i;
                continue;
            }
            b.append(childListTmpl
                    .replace("{childpascal}", ct.getPascalName())
                    .replace("{childcamel}", ct.getCamelName()));
            if (++i < t.getChildTables().size()) {
                b.append("\n");
            }
        }
        return b.toString();
    }

    private String generateGetters(Table t) {
        StringBuilder b = new StringBuilder();
        int i = 0;
        for (Column c : t.getColumns()) {
            b.append(getterTmpl
                    .replace("{datatype}", DataTypeUtil.resolvePrimitive(c))
                    .replace("{columnpascal}", c.getPascalName())
                    .replace("{columncamel}", c.getCamelName()));
            if (++i < t.getColumns().size()) {
                b.append("\n\n");
            }
        }
        for (Table ct : t.getChildTables()) {
            if (ct.isLooped() || ct.isJoined()) {
                continue;
            }
            b
                    .append("\n\n")
                    .append(getterTmpl
                            .replace("{datatype}", String.format("List<%s>", ct.getPascalName()))
                            .replace("{columnpascal}", String.format("%ss", ct.getPascalName()))
                            .replace("{columncamel}", String.format("%ss", ct.getCamelName())));
        }
        return b.toString();
    }

    private String generateSetters(Table t) {
        StringBuilder b = new StringBuilder();
        int i = 0;
        for (Column c : t.getColumns()) {
            b.append(setterTmpl
                    .replace("{datatype}", DataTypeUtil.resolvePrimitive(c))
                    .replace("{columnpascal}", c.getPascalName())
                    .replace("{columncamel}", c.getCamelName()));
            if (++i < t.getColumns().size()) {
                b.append("\n\n");
            }
        }
        for (Table ct : t.getChildTables()) {
            if (ct.isLooped() || ct.isJoined()) {
                continue;
            }
            b
                    .append("\n\n")
                    .append(setterTmpl
                            .replace("{datatype}", String.format("List<%s>", ct.getPascalName()))
                            .replace("{columnpascal}", String.format("%ss", ct.getPascalName()))
                            .replace("{columncamel}", String.format("%ss", ct.getCamelName())));
        }
        return b.toString();
    }

    @Override
    protected void loadTemplates() {
        classTmpl = loadTemplate("class");
        childListTmpl = loadTemplate("childlist");
        fieldTmpl = loadTemplate("field");
        getterTmpl = loadTemplate("getter");
        setterTmpl = loadTemplate("setter");
        implementationTmpl = loadTemplate("implementation");
        joiningImplementationTmpl = loadTemplate("joiningimplementation");
        repoObjTmpl = loadTemplate("repoobj");
        joinedRepoObjTmpl = loadTemplate("joinedrepoobj");
    }
}
