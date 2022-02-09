package com.mossonthetree.codegeneratorlib.python;

import com.mossonthetree.codegeneratorlib.Column;
import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.Generator;
import com.mossonthetree.codegeneratorlib.Table;

import java.util.HashMap;
import java.util.Map;

public class SqlDataStoreGenerator extends Generator {
  private String classTmpl;
  private String joinedClassTmpl;
  private String getChildListsTmpl;
  private String getByUniqueTmpl;
  private String getChildListTmpl;
  private String importChildModelTmpl;
  private String getForParentTmpl;

  public SqlDataStoreGenerator(Database db) {
    super(db, "./templates/python/sqldatastore");
  }

  @Override
  public Map<String, String> generate() {
    Map<String, String> files = new HashMap<>();
    for(Table t : db.getTables()) {
      files.put(t.getName() + "_store.py", generateDataStore(t));
    }
    return files;
  }

  private String generateDataStore(Table t) {
    if(t.isJoined() || t.isLooped()) {
      return joinedClassTmpl
        .replace("{tablepascal}", t.getPascalName())
        .replace("{tablename}", t.getName())
        .replace("{primaryname}", t.getPrimaryColumn().getForeignTable().getName())
        .replace("{primarypascal}", t.getPrimaryColumn().getForeignTable().getPascalName())
        .replace("{primarycolname}", t.getPrimaryColumn().getName())
        .replace("{secondaryname}", t.getSecondaryColumn().getForeignTable().getName())
        .replace("{secondarypascal}", t.getSecondaryColumn().getForeignTable().getPascalName())
        .replace("{secondarycolname}", t.getSecondaryColumn().getName())
        .replace("{projectname}", db.getProjectName());
    } else {
      return classTmpl
        .replace("{childimports}", generateChildImports(t))
        .replace("{getchildlists}", generateGetChildLists(t))
        .replace("{getchildlist}", generateGetChildList(t))
        .replace("{getforparent}", generateGetForParent(t))
        .replace("{getbyuniques}", generateSelectByUnique(t))
        .replace("{primaryfieldassignment}", generatePrimaryFieldAssignemnt(t))
        .replace("{tablepascal}", t.getPascalName())
        .replace("{tablename}", t.getName())
        .replace("{primaryname}", t.getPrimaryColumn().getName())
        .replace("{projectname}", db.getProjectName());
    }
  }

  private String generateChildImports(Table t) {
    StringBuilder b = new StringBuilder();
    for(Table ct: t.getNonJoinedNonLoopedTables()) {
      b
        .append("\n")
        .append(importChildModelTmpl
          .replace("{childtablepascal}", ct.getPascalName()));
    }
    return b.toString();
  }

  private String generateGetChildList(Table t) {
    StringBuilder b = new StringBuilder();
    for(Table ct: t.getNonJoinedNonLoopedTables()) {
      b
        .append("\n")
        .append(getChildListTmpl
          .replace("{childtablename}", ct.getName())
          .replace("{childtablepascal}", ct.getPascalName()));
    }
    return b.toString();
  }

  private String generateGetChildLists(Table t) {
    StringBuilder b = new StringBuilder();
    if(t.getNonJoinedNonLoopedTables().size() > 0) {
      b
        .append("\n")
        .append(getChildListsTmpl
          .replace("{getchildlist}", generateGetChildList(t)));
    }
    return b.toString();
  }

  private String generateGetForParent(Table t) {
    StringBuilder b = new StringBuilder();
    for(Table pt: t.getParentTables()) {
      b
        .append("\n\n\n")
        .append(getForParentTmpl
          .replace("{parenttablename}", pt.getName())
          .replace("{parentprimary}", pt.getPrimaryColumn().getName()));
    }
    return b.toString();
  }

  private String generateSelectByUnique(Table t) {
    StringBuilder b = new StringBuilder();
    for(Column c: t.getUniqueColumns()) {
      b
        .append("\n\n\n")
        .append(getByUniqueTmpl
          .replace("{colname}", c.getName())
          .replace("{getchildlists}", generateGetChildList(t)));
    }
    return b.toString();
  }

  private String generatePrimaryFieldAssignemnt(Table t) {
    return DataTypeUtil.resolvePrimaryAssignment(t.getPrimaryColumn());
  }

  @Override
  protected void loadTemplates() {
    classTmpl = loadTemplate("class");
    joinedClassTmpl = loadTemplate("joinedclass");
    getChildListsTmpl = loadTemplate("getchildlists");
    getByUniqueTmpl = loadTemplate("getbyunique");
    getChildListTmpl = loadTemplate("getchildlist");
    importChildModelTmpl = loadTemplate("importchildmodel");
    getForParentTmpl = loadTemplate("getforparent");
  }
}
