package com.mossonthetree.codegeneratorlib.java;

import com.mossonthetree.codegeneratorlib.Column;
import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.Generator;
import com.mossonthetree.codegeneratorlib.Table;

import java.util.HashMap;
import java.util.Map;

public class MapperGenerator extends Generator {
  private String classTmpl;
  private String getChildListTmpl;
  private String getFieldTmpl;
  private String joinedClassTmpl;
  private String setFieldTmpl;
  private String setChildListTmpl;

  public MapperGenerator(Database db) {
    super(db, "./templates/java/mappers");
  }

  @Override
  public Map<String, String> generate() {
    Map<String, String> files = new HashMap<>();
    for (Table t : db.getTables()) {
      files.put(t.getPascalName() + "Mapper.java", generateMapper(t));
    }
    return files;
  }

  private String generateMapper(Table t) {
    if (t.isJoined() || t.isLooped()) {
      return joinedClassTmpl
        .replace("{tablepascal}", t.getPascalName())
        .replace("{tablecamel}", t.getCamelName())
        .replace("{primarycolumnpascal}", t.getPrimaryColumn().getPascalName())
        .replace("{secondarycolumnpascal}", t.getSecondaryColumn().getPascalName())
        .replace("{rootname}", db.getProjectPath());
    } else {
      return classTmpl
        .replace("{getfields}", generateGetFields(t))
        .replace("{getchildlists}", generateGetChildLists(t))
        .replace("{setfields}", generateSetFields(t))
        .replace("{setchildlists}", generateSetChildLists(t))
        .replace("{tablepascal}", t.getPascalName())
        .replace("{tablecamel}", t.getCamelName())
        .replace("{rootname}", db.getProjectPath());
    }
  }

  private String generateGetFields(Table t) {
    StringBuilder b = new StringBuilder();
    int i = 0;
    for (Column c : t.getColumns()) {
      b
        .append(getFieldTmpl
          .replace("{columnpascal}", c.getPascalName()));
      if (++i < t.getColumns().size()) {
        b.append(", ");
      }
    }
    return b.toString();
  }

  private String generateGetChildLists(Table t) {
    StringBuilder b = new StringBuilder();
    for (Table ct : t.getChildTables()) {
      if (ct.isLooped() || ct.isJoined()) {
        continue;
      }
      b
        .append(", ")
        .append(getChildListTmpl
          .replace("{childpascal}", ct.getPascalName()));
    }
    return b.toString();
  }

  private String generateSetFields(Table t) {
    StringBuilder b = new StringBuilder();
    int i = 0;
    for (Column c : t.getColumns()) {
      b
        .append("\n")
        .append(setFieldTmpl
          .replace("{columnpascal}", c.getPascalName()));
    }
    return b.toString();
  }

  private String generateSetChildLists(Table t) {
    StringBuilder b = new StringBuilder();
    for (Table ct : t.getChildTables()) {
      if (ct.isJoined() || ct.isLooped()) {
        continue;
      }
      b
        .append("\n")
        .append(setChildListTmpl
          .replace("{childpascal}", ct.getPascalName()));
    }
    return b.toString();
  }

  @Override
  protected void loadTemplates() {
    classTmpl = loadTemplate("class");
    getChildListTmpl = loadTemplate("getchildlist");
    getFieldTmpl = loadTemplate("getfield");
    joinedClassTmpl = loadTemplate("joinedclass");
    setFieldTmpl = loadTemplate("setfield");
    setChildListTmpl = loadTemplate("setchildlist");
  }
}

