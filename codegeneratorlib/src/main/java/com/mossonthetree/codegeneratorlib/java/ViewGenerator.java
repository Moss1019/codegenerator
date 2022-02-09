package com.mossonthetree.codegeneratorlib.java;

import com.mossonthetree.codegeneratorlib.Column;
import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.Generator;
import com.mossonthetree.codegeneratorlib.Table;

import java.util.HashMap;
import java.util.Map;

public class ViewGenerator extends Generator {
  private String classTmpl;
  private String childListTmpl;
  private String childListArgTmpl;
  private String childListAssignTmpl;
  private String fieldTmpl;
  private String fieldArgTmpl;
  private String fieldAssignTmpl;
  private String getChildListTmpl;
  private String getFieldTmpl;
  private String joinedClassTmpl;

  public ViewGenerator(Database db) {
    super(db, "./templates/java/views");
  }

  public Map<String, String> generate() {
    Map<String, String> files = new HashMap<>();
    for (Table t : db.getTables()) {
      files.put(t.getPascalName() + "View.java", generateView(t));
    }
    return files;
  }

  private String generateView(Table t) {
    if (t.isJoined() || t.isLooped()) {
      return joinedClassTmpl
        .replace("{primarycolumncamel}", t.getPrimaryColumn().getCamelName())
        .replace("{secondarycolumncamel}", t.getSecondaryColumn().getCamelName())
        .replace("{primarycolumnpascal}", t.getPrimaryColumn().getPascalName())
        .replace("{secondarycolumnpascal}", t.getSecondaryColumn().getPascalName())
        .replace("{primarydatatype}", DataTypeUtil.resolvePrimitive(t.getPrimaryColumn()))
        .replace("{secondarydatatype}", DataTypeUtil.resolvePrimitive(t.getSecondaryColumn()))
        .replace("{tablepascal}", t.getPascalName())
        .replace("{rootname}", db.getProjectPath());
    } else {
      return classTmpl
        .replace("{fields}", generateFields(t))
        .replace("{childlists}", generateChildLists(t))
        .replace("{fieldargs}", generateFieldArgs(t))
        .replace("{childlistargs}", generateChildListArgs(t))
        .replace("{fieldassigns}", generateFieldAssigns(t))
        .replace("{childlistassigns}", generateListAssigns(t))
        .replace("{fieldgetters}", generateFieldGetters(t))
        .replace("{childlistgetters}", generateListGetters(t))
        .replace("{tablepascal}", t.getPascalName())
        .replace("{rootname}", db.getProjectPath());
    }
  }

  private String generateFields(Table t) {
    StringBuilder b = new StringBuilder();
    for (Column c : t.getColumns()) {
      b
        .append("\n")
        .append(fieldTmpl
          .replace("{datatype}", DataTypeUtil.resolvePrimitive(c))
          .replace("{columncamel}", c.getCamelName())
          .replace("{default}", DataTypeUtil.resolveDefault(c)));
    }
    return b.toString();
  }

  private String generateChildLists(Table t) {
    StringBuilder b = new StringBuilder();
    for (Table ct : t.getChildTables()) {
      if (ct.isJoined() || ct.isLooped()) {
        continue;
      }
      b
        .append("\n")
        .append(childListTmpl
          .replace("{childpascal}", ct.getPascalName())
          .replace("{childcamel}", ct.getCamelName()));
    }
    return b.toString();
  }

  private String generateFieldArgs(Table t) {
    StringBuilder b = new StringBuilder();
    int i = 0;
    for (Column c : t.getColumns()) {
      b
        .append(fieldArgTmpl
          .replace("{datatype}", DataTypeUtil.resolvePrimitive(c))
          .replace("{columncamel}", c.getCamelName()));
      if (++i < t.getColumns().size()) {
        b.append(", ");
      }
    }
    return b.toString();
  }

  private String generateChildListArgs(Table t) {
    StringBuilder b = new StringBuilder();
    for (Table ct : t.getChildTables()) {
      if (ct.isJoined() || ct.isLooped()) {
        continue;
      }
      b
        .append(", ")
        .append(childListArgTmpl
          .replace("{childpascal}", ct.getPascalName())
          .replace("{childcamel}", ct.getCamelName()));
    }
    return b.toString();
  }

  private String generateFieldAssigns(Table t) {
    StringBuilder b = new StringBuilder();
    int i = 0;
    for (Column c : t.getColumns()) {
      b
        .append(fieldAssignTmpl
          .replace("{columncamel}", c.getCamelName()));
      if (++i < t.getColumns().size()) {
        b.append("\n");
      }
    }
    return b.toString();
  }

  private String generateListAssigns(Table t) {
    StringBuilder b = new StringBuilder();
    for (Table ct : t.getChildTables()) {
      if (ct.isJoined() || ct.isLooped()) {
        continue;
      }
      b
        .append("\n")
        .append(childListAssignTmpl
          .replace("{childcamel}", ct.getCamelName()));
    }
    return b.toString();
  }

  private String generateFieldGetters(Table t) {
    StringBuilder b = new StringBuilder();
    for (Column c : t.getColumns()) {
      b
        .append("\n\n")
        .append(getFieldTmpl
          .replace("{datatype}", DataTypeUtil.resolvePrimitive(c))
          .replace("{columnpascal}", c.getPascalName())
          .replace("{columncamel}", c.getCamelName()));
    }
    return b.toString();
  }

  private String generateListGetters(Table t) {
    StringBuilder b = new StringBuilder();
    for (Table ct : t.getChildTables()) {
      if (ct.isJoined() || ct.isLooped()) {
        continue;
      }
      b
        .append("\n\n")
        .append(getChildListTmpl
          .replace("{childpascal}", ct.getPascalName())
          .replace("{childcamel}", ct.getCamelName()));
    }
    return b.toString();
  }

  @Override
  protected void loadTemplates() {
    classTmpl = loadTemplate("class");
    childListTmpl = loadTemplate("childlist");
    childListArgTmpl = loadTemplate("childlistarg");
    childListAssignTmpl = loadTemplate("childlistassign");
    fieldTmpl = loadTemplate("field");
    fieldArgTmpl = loadTemplate("fieldarg");
    fieldAssignTmpl = loadTemplate("fieldassign");
    getChildListTmpl = loadTemplate("getchildlist");
    getFieldTmpl = loadTemplate("getfield");
    joinedClassTmpl = loadTemplate("joinedclass");
  }
}
