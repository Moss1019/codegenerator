package com.mossonthetree.codegeneratorlib.typescript;

import com.mossonthetree.codegeneratorlib.Column;
import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.Generator;
import com.mossonthetree.codegeneratorlib.Table;

import java.util.HashMap;
import java.util.Map;

public class TypeScriptModelGenerator extends Generator {
  private String joinedClassTmpl;
  private String classTmpl;
  private String fieldTmpl;
  private String importTmpl;
  private String childListTmpl;

  public TypeScriptModelGenerator(Database db) {
    super(db, "./templates/typescriptmodels");
  }

  @Override
  public Map<String, String> generate() {
    Map<String, String> files = new HashMap<>();
    for (Table t : db.getTables()) {
      files.put(t.getLowerName() + ".ts", generateModel(t));
    }
    return files;
  }

  private String generateModel(Table t) {
    if (t.isJoined() || t.isLooped()) {
      return joinedClassTmpl
        .replace("{tablepascal}", t.getPascalName())
        .replace("{primarycamel}", t.getPrimaryColumn().getCamelName())
        .replace("{primarydatatype}", DataTypeUtil.resolveType(t.getPrimaryColumn()))
        .replace("{secondarycamel}", t.getSecondaryColumn().getCamelName())
        .replace("{secondarydatatype}", DataTypeUtil.resolveType(t.getSecondaryColumn()));
    } else {
      return classTmpl
        .replace("{childimports}", generateChildImports(t))
        .replace("{fields}", generateFields(t))
        .replace("{childlists}", generateChildLists(t))
        .replace("{tablepascal}", t.getPascalName());
    }
  }

  private String generateChildImports(Table t) {
    StringBuilder b = new StringBuilder();
    for (Table ct : t.getChildTables()) {
      if (ct.isLooped() || ct.isJoined()) {
        continue;
      }
      b
        .append(importTmpl
          .replace("{childpascal}", ct.getPascalName())
          .replace("{childlower}", ct.getLowerName()))
        .append("\n");
    }
    return b.toString();
  }

  private String generateFields(Table t) {
    StringBuilder b = new StringBuilder();
    for (Column c : t.getColumns()) {
      b
        .append("\n\t")
        .append(fieldTmpl
          .replace("{colcamel}", c.getCamelName())
          .replace("{coldatatype}", DataTypeUtil.resolveType(c)));
    }
    return b.toString();
  }

  private String generateChildLists(Table t) {
    StringBuilder b = new StringBuilder();
    for (Table ct : t.getChildTables()) {
      if (ct.isLooped() || ct.isJoined()) {
        continue;
      }
      b
        .append("\n\t")
        .append(childListTmpl
          .replace("{childcamel}", ct.getCamelName())
          .replace("{childpascal}", ct.getPascalName()));
    }
    return b.toString();
  }

  @Override
  protected void loadTemplates() {
    joinedClassTmpl = loadTemplate("joinedclass");
    classTmpl = loadTemplate("class");
    fieldTmpl = loadTemplate("field");
    importTmpl = loadTemplate("import");
    childListTmpl = loadTemplate("childlist");
  }
}
