package com.mossonthetree.codegeneratorlib.python;

import com.mossonthetree.codegeneratorlib.Column;
import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.Generator;
import com.mossonthetree.codegeneratorlib.Table;

import java.util.HashMap;
import java.util.Map;

public class ViewGenerator extends Generator {
  private String classTmpl;
  private String joinedClassTmpl;
  private String getForParentTmpl;
  private String getByUniqueTmpl;

  public ViewGenerator(Database db) {
    super(db, "./templates/python/views");
  }

  @Override
  public Map<String, String> generate() {
    Map<String, String> files = new HashMap<>();
    for (Table t : db.getTables()) {
      files.put(t.getName() + "_views.py" ,generateViewsFile(t));
    }
    return files;
  }

  private String generateViewsFile(Table t) {
    if (t.isLooped() || t.isJoined()) {
      return joinedClassTmpl
        .replace("{tablename}", t.getName())
        .replace("{tablepascal}", t.getPascalName())
        .replace("{primarypascal}", t.getPrimaryColumn().getForeignTable().getPascalName())
        .replace("{primarycolname}", t.getPrimaryColumn().getName())
        .replace("{primaryname}", t.getPrimaryColumn().getForeignTable().getName())
        .replace("{secondarycolname}", t.getSecondaryColumn().getName())
        .replace("{projectname}", db.getProjectName());
    } else {
      return classTmpl
        .replace("{getforparent}", generateGetForParent(t))
        .replace("{getbyuniques}", generateSelectByUniques(t))
        .replace("{tablename}", t.getName())
        .replace("{primaryname}", t.getPrimaryColumn().getName())
        .replace("{tablepascal}", t.getPascalName())
        .replace("{projectname}", db.getProjectName());
    }
  }

  private String generateGetForParent(Table t) {
    StringBuilder b = new StringBuilder();
    for(Table pt: t.getParentTables()) {
      b
        .append("\n\n\n")
        .append(getForParentTmpl
          .replace("{parentname}", pt.getName())
          .replace("{parentprimary}", pt.getPrimaryColumn().getName()));
    }
    return b.toString();
  }

  private String generateSelectByUniques(Table t) {
    StringBuilder b = new StringBuilder();
    for(Column c: t.getUniqueColumns()) {
      b
        .append("\n\n\n")
        .append(getByUniqueTmpl
          .replace("{colname}", c.getName()));
    }
    return b.toString();
  }

  @Override
  protected void loadTemplates() {
    classTmpl = loadTemplate("class");
    joinedClassTmpl = loadTemplate("joinedclass");
    getForParentTmpl = loadTemplate("getforparent");
    getByUniqueTmpl = loadTemplate("getbyunique");
  }
}
