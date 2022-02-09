package com.mossonthetree.codegeneratorlib.java;

import com.mossonthetree.codegeneratorlib.Column;
import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.Generator;
import com.mossonthetree.codegeneratorlib.Table;

import java.util.HashMap;
import java.util.Map;

public class JpaMapperGenerator extends Generator {
  private String joinedClassTmpl;
  private String classTmpl;
  private String getChildListTmpl;
  private String getFieldTmpl;
  private String getParentTmpl;
  private String setChildListTmpl;
  private String setFieldTmpl;
  private String setParentTmpl;

  public JpaMapperGenerator(Database db) {
    super(db, "./templates/java/sqlmappers");
  }

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
        .replace("{primarypascal}", t.getPrimaryColumn().getPascalName())
        .replace("{secondarypascal}", t.getSecondaryColumn().getPascalName())
        .replace("{primarydatatype}", DataTypeUtil.resolvePrimitive(t.getPrimaryColumn()))
        .replace("{secondarydatatype}", DataTypeUtil.resolvePrimitive(t.getSecondaryColumn()))
        .replace("{rootname}", db.getProjectPath());
    } else {
      return classTmpl
        .replace("{getfields}", generateGetFields(t))
        .replace("{getparentids}", generateGetParentIds(t))
        .replace("{getchildlists}", generateGetChildLists(t))
        .replace("{setfields}", generateSetFields(t))
        .replace("{setparentids}", generateSetParentIds(t))
        .replace("{setchildlists}", generateSetChildLists(t))
        .replace("{tablecamel}", t.getCamelName())
        .replace("{tablepascal}", t.getPascalName())
        .replace("{rootname}", db.getProjectPath());
    }
  }

  private String generateGetFields(Table t) {
    StringBuilder b = new StringBuilder();
    int i = 0;
    int amount = (int) t.getColumns().stream().filter(c -> !c.isForeign()).count();
    for (Column c : t.getColumns()) {
      if (c.isForeign()) {
        continue;
      }
      b.append(getFieldTmpl
        .replace("{columnpascal}", c.getPascalName()));
      if (++i < amount) {
        b.append(", ");
      }
    }
    return b.toString();
  }

  private String generateGetParentIds(Table t) {
    StringBuilder b = new StringBuilder();
    for (Table pt : t.getParentTables()) {
      b
        .append(", ")
        .append(getParentTmpl
          .replace("{parentpascal}", pt.getPascalName())
          .replace("{parentprimarypascal}", pt.getPrimaryColumn().getPascalName()));
    }
    return b.toString();
  }

  private String generateGetChildLists(Table t) {
    StringBuilder b = new StringBuilder();
    for (Table ct : t.getChildTables()) {
      if (ct.isJoined() || ct.isLooped()) {
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
      if (c.isForeign()) {
        ++i;
        continue;
      }
      b
        .append("\n")
        .append(setFieldTmpl
          .replace("{columnpascal}", c.getPascalName()));
    }
    return b.toString();
  }

  private String generateSetParentIds(Table t) {
    StringBuilder b = new StringBuilder();
    for (Table pt : t.getParentTables()) {
      b
        .append("\n")
        .append(setParentTmpl
          .replace("{tablecamel}", t.getCamelName())
          .replace("{parentprimarypascal}", pt.getPrimaryColumn().getPascalName()));
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
    joinedClassTmpl = loadTemplate("joinedclass");
    classTmpl = loadTemplate("class");
    getChildListTmpl = loadTemplate("getchildlist");
    getFieldTmpl = loadTemplate("getfield");
    getParentTmpl = loadTemplate("getparent");
    setChildListTmpl = loadTemplate("setchildlist");
    setFieldTmpl = loadTemplate("setfield");
    setParentTmpl = loadTemplate("setparent");
  }
}