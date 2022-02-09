package com.mossonthetree.codegeneratorlib.typescript;

import java.util.*;

import com.mossonthetree.codegeneratorlib.*;

public class AxiosGenerator extends Generator {
  private String axiosTmpl;
  private String joinedClassTmpl;
  private String classTmpl;
  private String selectByUniqueTmpl;
  private String selectOfParentTmpl;

  public AxiosGenerator(Database db) {
    super(db, "./templates/axios");
  }

  public Map<String, String> generate() {
    Map<String, String> files = new HashMap<>();
    files.put("axios.ts", axiosTmpl);
    for(Table t: db.getTables()) {
      files.put(t.getLowerName() + ".ts", generateHttpCalls(t));
    }
    return files;
  }

  private String generateHttpCalls(Table t) {
    if(t.isJoined() || t.isLooped()) {
      return joinedClassTmpl
        .replace("{tablepascal}", t.getPascalName())
        .replace("{tablecamel}", t.getCamelName())
        .replace("{primarycolcamel}", t.getPrimaryColumn().getCamelName())
        .replace("{secondarycolcamel}", t.getSecondaryColumn().getCamelName())
        .replace("{primarypascal}", t.getPrimaryColumn().getForeignTable().getPascalName());
    } else {
      return classTmpl
        .replace("{primarycolcamel}", t.getPrimaryColumn().getCamelName())
        .replace("{selectbyunique}", generateSelectByUnique(t))
        .replace("{selectofparent}", generateSelectOfParent(t))
        .replace("{tablepascal}", t.getPascalName())
        .replace("{tablecamel}", t.getCamelName());
    }
  }

  private String generateSelectByUnique(Table t) {
    StringBuilder b = new StringBuilder();
    for(Column c: t.getUniqueColumns()) {
      b
        .append("\n\n")
        .append(selectByUniqueTmpl
          .replace("{columnpascal}", c.getPascalName())
          .replace("{columncamel}", c.getCamelName()));
    }
    return b.toString();
  }

  private String generateSelectOfParent(Table t) {
    StringBuilder b = new StringBuilder();
    for(Table pt: t.getParentTables()) {
      b
        .append("\n\n")
        .append(selectOfParentTmpl
          .replace("{parentpascal}", pt.getPascalName())
          .replace("{parentprimarycamel}", pt.getPrimaryColumn().getCamelName()));
    }
    return b.toString();
  }

  @Override
  protected void loadTemplates() {
    axiosTmpl = loadTemplate("axios");
    joinedClassTmpl = loadTemplate("joinedclass");
    classTmpl = loadTemplate("class");
    selectByUniqueTmpl = loadTemplate("selectbyunique");
    selectOfParentTmpl = loadTemplate("selectofparent");
  }
}
