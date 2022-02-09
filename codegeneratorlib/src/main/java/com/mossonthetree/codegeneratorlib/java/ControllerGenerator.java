package com.mossonthetree.codegeneratorlib.java;

import com.mossonthetree.codegeneratorlib.Column;
import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.Generator;
import com.mossonthetree.codegeneratorlib.Table;

import java.util.HashMap;
import java.util.Map;

public class ControllerGenerator extends Generator {
  private String classTmpl;
  private String selectForParentTmpl;
  private String selectByUniqueTmpl;
  private String joinedClassTmpl;

  public ControllerGenerator(Database db) {
    super(db, "./templates/java/controllers");
  }

  public Map<String, String> generate() {
    Map<String, String> files = new HashMap<>();
    for (Table t : db.getTables()) {
      files.put(t.getPascalName() + "Controller.java", generateController(t));
    }
    return files;
  }

  private String generateController(Table t) {
    if (t.isJoined() || t.isLooped()) {
      return joinedClassTmpl
        .replace("{parentpascal}", t.getPrimaryColumn().getForeignTable().getPascalName())
        .replace("{parentprimarylower}", t.getPrimaryColumn().getLowerName())
        .replace("{primarylower}", t.getPrimaryColumn().getLowerName())
        .replace("{secondarylower}", t.getSecondaryColumn().getLowerName())
        .replace("{tablepascal}", t.getPascalName())
        .replace("{tablelower}", t.getLowerName())
        .replace("{primarydatatype}", DataTypeUtil.resolvePrimitive(t.getPrimaryColumn()))
        .replace("{secondarydatatype}", DataTypeUtil.resolvePrimitive(t.getSecondaryColumn()))
        .replace("{secondarypascal}", t.getSecondaryColumn().getForeignTable().getPascalName())
        .replace("{rootname}", db.getProjectPath());
    } else {
      return classTmpl
        .replace("{selectforparent}", generateSelectForParent(t))
        .replace("{selectbyunique}", generateSelectByUnique(t))
        .replace("{tablepascal}", t.getPascalName())
        .replace("{tablelower}", t.getLowerName())
        .replace("{primarydatatype}", DataTypeUtil.resolvePrimitive(t.getPrimaryColumn()))
        .replace("{rootname}", db.getProjectPath());
    }
  }

  private String generateSelectForParent(Table t) {
    StringBuilder b = new StringBuilder();
    int i = 0;
    for (Table pt : t.getParentTables()) {
      b
        .append("\n\n")
        .append(selectForParentTmpl
          .replace("{parentprimarydatatype}", DataTypeUtil.resolvePrimitive(pt.getPrimaryColumn()))
          .replace("{secondarypascal}", pt.getPascalName())
          .replace("{parentpascal}", pt.getPascalName())
          .replace("{parentprimarylower}", pt.getPrimaryColumn().getLowerName()));
    }
    return b.toString();
  }

  private String generateSelectByUnique(Table t) {
    StringBuilder b = new StringBuilder();
    int i = 0;
    for (Column c : t.getUniqueColumns()) {
      b
        .append("\n\n")
        .append(selectByUniqueTmpl
          .replace("{columnpascal}", c.getPascalName())
          .replace("{pascallower}", c.getLowerName()));
    }
    return b.toString();
  }

  @Override
  protected void loadTemplates() {
    classTmpl = loadTemplate("class");
    selectForParentTmpl = loadTemplate("selectforparent");
    selectByUniqueTmpl = loadTemplate("selectbyunique");
    joinedClassTmpl = loadTemplate("joiningclass");
  }
}