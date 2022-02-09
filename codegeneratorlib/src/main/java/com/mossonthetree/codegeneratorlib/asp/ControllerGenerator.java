package com.mossonthetree.codegeneratorlib.asp;

import com.mossonthetree.codegeneratorlib.Column;
import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.Generator;
import com.mossonthetree.codegeneratorlib.Table;

import java.util.*;

public class ControllerGenerator extends Generator {
  private String classTmpl;
  private String getByUniqueTmpl;
  private String getForParentTmpl;
  private String joiningClassTmpl;

  public ControllerGenerator(Database db) {
    super(db, "./templates/dotnet/controllers");
  }

  public Map<String, String> generate() {
    Map<String, String> files = new HashMap<>();
    for (Table t : db.getTables()) {
      files.put(t.getPascalName() + "Controller.cs", generateController(t));
    }
    return files;
  }

  private String generateController(Table t) {
    if (t.isJoined() || t.isLooped()) {
      return joiningClassTmpl
        .replace("{rootname}", db.getProjectPath())
        .replace("{tablelower}", t.getLowerName())
        .replace("{tablepascal}", t.getPascalName());
    } else {
      return classTmpl
        .replace("{primarycamel}", t.getPrimaryColumn().getCamelName())
        .replace("{rootname}", db.getProjectPath())
        .replace("{getforparent}", generateGetForParent(t))
        .replace("{getbyunique}", generateGetByUnqiue(t))
        .replace("{tablepascal}", t.getPascalName())
        .replace("{tablelower}", t.getLowerName());
    }
  }

  private String generateGetForParent(Table t) {
    StringBuilder b = new StringBuilder();
    int i = 0;
    for (Table pt : t.getParentTables()) {
      b
        .append("\n\n")
        .append(getForParentTmpl
          .replace("{primarytablelower}", pt.getLowerName())
          .replace("{parenttablepascal}", pt.getPascalName())
          .replace("{primarycamel}", pt.getPrimaryColumn().getCamelName()));
    }
    return b.toString();
  }

  private String generateGetByUnqiue(Table t) {
    StringBuilder b = new StringBuilder();
    int i = 0;
    for (Column c : t.getUniqueColumns()) {
      b
        .append(getByUniqueTmpl
          .replace("{columnlower}", c.getLowerName())
          .replace("{columnpascal}", c.getPascalName()));
      if (++i < t.getUniqueColumns().size()) {
        b.append("\n\n");
      }
    }
    return b.toString();
  }

  @Override
  protected void loadTemplates() {
    classTmpl = loadTemplate("class");
    getByUniqueTmpl = loadTemplate("getbyunique");
    getForParentTmpl = loadTemplate("getforparent");
    joiningClassTmpl = loadTemplate("joiningclass");
  }
}