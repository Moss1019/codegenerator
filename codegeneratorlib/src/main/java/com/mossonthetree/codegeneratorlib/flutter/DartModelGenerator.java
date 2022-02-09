package com.mossonthetree.codegeneratorlib.flutter;

import com.mossonthetree.codegeneratorlib.Column;
import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.Generator;
import com.mossonthetree.codegeneratorlib.Table;

import java.util.HashMap;
import java.util.Map;

public class DartModelGenerator extends Generator {
  private String classTmpl;
  private String fieldTmpl;
  private String argumentTmpl;
  private String assignTmpl;
  private String jsonAssignTmpl;
  private String joinedClassTmpl;
  private String childListArgumentTmpl;
  private String childListFieldTmpl;
  private String childListJsonAssignTmpl;
  private String childListAssign;
  private String importTmpl;

  public DartModelGenerator(Database db) {
    super(db, "./templates/flutter/models");
  }

  @Override
  public Map<String, String> generate() {
    Map<String, String> files = new HashMap<>();
    for (Table t : db.getTables()) {
      files.put(t.getName() + ".dart", generateModel(t));
    }
    return files;
  }

  private String generateModel(Table t) {
    if (t.isJoined() || t.isLooped()) {
      return joinedClassTmpl
        .replace("{tablepascal}", t.getPascalName())
        .replace("{primarycoltype}", DataTypeUtil.resolvePrimitive(t.getPrimaryColumn()))
        .replace("{primarycolcamel}", t.getPrimaryColumn().getCamelName())
        .replace("{secondarycoltype}", DataTypeUtil.resolvePrimitive(t.getPrimaryColumn()))
        .replace("{secondarycolcamel}", t.getSecondaryColumn().getCamelName())
        .replace("{primarydefault}", DataTypeUtil.resolveDefault(t.getPrimaryColumn()))
        .replace("{secondarydefault}", DataTypeUtil.resolveDefault(t.getSecondaryColumn()));
    } else {
      return classTmpl
        .replace("{childimports}", generateImports(t))
        .replace("{fields}", generateFields(t))
        .replace("{arguments}", generateArguments(t))
        .replace("{jsonassigns}", generateJsonAssigns(t))
        .replace("{assigns}", generateAssigns(t))
        .replace("{tablepascal}", t.getPascalName());
    }
  }

  private String generateImports(Table t) {
    StringBuilder b = new StringBuilder();
    for (Table ct : t.getChildTables()) {
      if (ct.isLooped() || ct.isJoined()) {
        continue;
      }
      b
        .append("\n")
        .append(importTmpl
          .replace("{tablename}", ct.getName()));
    }
    return b.toString();
  }

  private String generateFields(Table t) {
    StringBuilder b = new StringBuilder();
    for (Column c : t.getColumns()) {
      b
        .append("\n")
        .append(fieldTmpl
          .replace("{coltype}", DataTypeUtil.resolvePrimitive(c))
          .replace("{colcamel}", c.getCamelName()));
    }
    for (Table ct : t.getChildTables()) {
      if (ct.isJoined() || ct.isLooped()) {
        continue;
      }
      b
        .append("\n")
        .append(childListFieldTmpl
          .replace("{tablepascal}", ct.getPascalName())
          .replace("{tablecamel}", ct.getCamelName()));
    }
    return b.toString();
  }

  private String generateArguments(Table t) {
    StringBuilder b = new StringBuilder();
    for (Column c : t.getColumns()) {
      b
        .append("\n")
        .append(argumentTmpl
          .replace("{colcamel}", c.getCamelName())
          .replace("{coldefault}", DataTypeUtil.resolveDefault(c)))
        .append(",");
    }
    for (Table ct : t.getChildTables()) {
      if (ct.isJoined() || ct.isLooped()) {
        continue;
      }
      b
        .append("\n")
        .append(childListArgumentTmpl
          .replace("{tablecamel}", ct.getCamelName()))
        .append(",");
    }
    return b.toString();
  }

  private String generateJsonAssigns(Table t) {
    StringBuilder b = new StringBuilder();
    int index = 0;
    for (Column c : t.getColumns()) {
      b
        .append("\n")
        .append(jsonAssignTmpl
          .replace("{colcamel}", c.getCamelName()))
        .append(",");
    }
    for (Table ct : t.getChildTables()) {
      if (ct.isJoined() || ct.isLooped()) {
        continue;
      }
      b
        .append("\n")
        .append(childListJsonAssignTmpl
          .replace("{tablecamel}", ct.getCamelName()))
        .append(",");
    }
    return b.toString();
  }

  private String generateAssigns(Table t) {
    StringBuilder b = new StringBuilder();
    for (Column c : t.getColumns()) {
      b
        .append("\n")
        .append(assignTmpl
          .replace("{colcamel}", c.getCamelName()));
      b.append(",");
    }
    for (Table ct : t.getChildTables()) {
      if (ct.isJoined() || ct.isLooped()) {
        continue;
      }
      b
        .append("\n")
        .append(childListAssign
          .replace("{tablecamel}", ct.getCamelName()))
        .append(",");
    }
    return b.toString();
  }

  @Override
  protected void loadTemplates() {
    classTmpl = loadTemplate("class");
    fieldTmpl = loadTemplate("field");
    argumentTmpl = loadTemplate("argument");
    assignTmpl = loadTemplate("assign");
    jsonAssignTmpl = loadTemplate("jsonassign");
    joinedClassTmpl = loadTemplate("joinedclass");
    childListArgumentTmpl = loadTemplate("childlistargument");
    childListFieldTmpl = loadTemplate("childlistfield");
    childListJsonAssignTmpl = loadTemplate("childlistjsonassign");
    childListAssign = loadTemplate("childlistassign");
    importTmpl = loadTemplate("import");
  }
}
