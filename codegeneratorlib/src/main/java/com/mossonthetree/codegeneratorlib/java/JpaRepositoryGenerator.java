package com.mossonthetree.codegeneratorlib.java;

import com.mossonthetree.codegeneratorlib.Column;
import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.Generator;
import com.mossonthetree.codegeneratorlib.Table;

import java.util.HashMap;
import java.util.Map;

public class JpaRepositoryGenerator extends Generator {
  private String joinedClassTmpl;
  private String classTmpl;
  private String setParentTmpl;
  private String selectByUniqueTmpl;
  private String selectOfParentTmpl;
  private String setColumnTmpl;

  public JpaRepositoryGenerator(Database db) {
    super(db, "./templates/java/sqldb");
  }

  @Override
  public Map<String, String> generate() {
    Map<String, String> files = new HashMap<>();
    for (Table t : db.getTables()) {
      files.put(t.getPascalName() + "Repository.java", generateRepository(t));
    }
    return files;
  }

  private String generateRepository(Table t) {
    if (t.isJoined() || t.isLooped()) {
      return joinedClassTmpl
        .replace("{tablepascal}", t.getPascalName())
        .replace("{tablecamel}", t.getCamelName())
        .replace("{primarypascal}", t.getPrimaryColumn().getForeignTable().getPascalName())
        .replace("{primarycolpascal}", t.getPrimaryColumn().getPascalName())
        .replace("{secondarypascal}", t.getSecondaryColumn().getForeignTable().getPascalName())
        .replace("{secondarycolpascal}", t.getSecondaryColumn().getPascalName())
        .replace("{primarycolcamel}", t.getPrimaryColumn().getCamelName())
        .replace("{secondarycolcamel}", t.getSecondaryColumn().getCamelName())
        .replace("{primarydatatype}", DataTypeUtil.resolvePrimitive(t.getPrimaryColumn()))
        .replace("{secondarydatatype}", DataTypeUtil.resolvePrimitive(t.getSecondaryColumn()))
        .replace("{rootname}", db.getProjectPath());
    } else {
      return classTmpl
        .replace("{setparents}", generateSetParents(t))
        .replace("{selectbyunique}", generateSelectByUnique(t))
        .replace("{selectofparent}", generateSelectOfParent(t))
        .replace("{setcolumns}", generateSetColumns(t))
        .replace("{primarycolcamel}", t.getPrimaryColumn().getCamelName())
        .replace("{primarycolpascal}", t.getPrimaryColumn().getPascalName())
        .replace("{primarydatatype}", DataTypeUtil.resolvePrimitive(t.getPrimaryColumn()))
        .replace("{tablepascal}", t.getPascalName())
        .replace("{tablecamel}", t.getCamelName())
        .replace("{rootname}", db.getProjectPath());
    }
  }

  private String generateSetParents(Table t) {
    StringBuilder b = new StringBuilder();
    for (Table pt : t.getParentTables()) {
      b
        .append("\n\t\t")
        .append(setParentTmpl
          .replace("{parentpascal}", pt.getPascalName())
          .replace("{parentprimarypascal}", pt.getPrimaryColumn().getPascalName()));
    }
    return b.toString();
  }

  private String generateSetColumns(Table t) {
    StringBuilder b = new StringBuilder();
    for (Column c : t.getColumns()) {
      if (c.isPrimary() || c.isForeign()) {
        continue;
      }
      b
        .append("\n")
        .append(setColumnTmpl
          .replace("{columnpascal}", c.getPascalName()));
    }
    return b.toString();
  }

  private String generateSelectByUnique(Table t) {
    StringBuilder b = new StringBuilder();
    for (Column c : t.getUniqueColumns()) {
      b
        .append("\n\n")
        .append(selectByUniqueTmpl
          .replace("{colname}", c.getName())
          .replace("{coldatatype}", DataTypeUtil.resolvePrimitive(c))
          .replace("{columnpascal}", c.getPascalName()));
    }
    return b.toString();
  }

  private String generateSelectOfParent(Table t) {
    StringBuilder b = new StringBuilder();
    for (Table pt : t.getParentTables()) {
      b
        .append("\n\n")
        .append(selectOfParentTmpl
          .replace("{parentpascal}", pt.getPascalName())
          .replace("{parentprimarydatatype}", DataTypeUtil.resolvePrimitive(pt.getPrimaryColumn()))
          .replace("{parentprimarycamel}", pt.getPrimaryColumn().getCamelName()));
    }
    return b.toString();
  }

  @Override
  protected void loadTemplates() {
    joinedClassTmpl = loadTemplate("joinedclass");
    classTmpl = loadTemplate("class");
    setParentTmpl = loadTemplate("setparent");
    selectByUniqueTmpl = loadTemplate("selectbyunique");
    selectOfParentTmpl = loadTemplate("selectofparent");
    setColumnTmpl = loadTemplate("setcolumn");
  }
}
