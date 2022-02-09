package com.mossonthetree.codegeneratorlib.asp;

import com.mossonthetree.codegeneratorlib.Column;
import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.Generator;
import com.mossonthetree.codegeneratorlib.Table;

import java.util.*;

public class ServiceGenerator extends Generator {
  private String interfaceTmpl;
  private String iSelectByUniqueTmpl;
  private String iSelectForParentTmpl;
  private String classTmpl;
  private String selectByUniqueTmpl;
  private String selectForParentTmpl;
  private String joinedClassTmpl;
  private String joinedInterfaceTmpl;

  public ServiceGenerator(Database db) {
    super(db, "./templates/dotnet/services");
  }

  public Map<String, String> generate() {
    Map<String, String> files = new HashMap<>();
    for (Table t : db.getTables()) {
      files.put("I" + t.getPascalName() + "Service.cs", generateInterface(t));
    }
    for (Table t : db.getTables()) {
      files.put(t.getPascalName() + "Service.cs", generateSevice(t));
    }
    return files;
  }

  private String generateInterface(Table t) {
    if (t.isLooped() || t.isJoined()) {
      return joinedInterfaceTmpl
        .replace("{primarycamel}", t.getPrimaryColumn().getCamelName())
        .replace("{secondarycamel}", t.getSecondaryColumn().getCamelName())
        .replace("{secondarytablepascal}", t.getSecondaryColumn().getForeignTable().getPascalName())
        .replace("{rootname}", db.getProjectPath())
        .replace("{tablepascal}", t.getPascalName());
    } else {
      return interfaceTmpl
        .replace("{selectforparent}", generateInterfaceSelectForParent(t))
        .replace("{selectbyunique}", generateInterfaceSelectByUnique(t))
        .replace("{rootname}", db.getProjectPath())
        .replace("{tablepascal}", t.getPascalName());
    }
  }

  private String generateInterfaceSelectForParent(Table t) {
    StringBuilder b = new StringBuilder();
    int i = 0;
    for (Table pt : t.getParentTables()) {
      b
        .append("\n\n")
        .append(iSelectForParentTmpl
          .replace("{primarypascal}", pt.getPascalName())
          .replace("{primarycamel}", pt.getPrimaryColumn().getCamelName()));
    }
    return b.toString();
  }

  private String generateInterfaceSelectByUnique(Table t) {
    StringBuilder b = new StringBuilder();
    int i = 0;
    for (Column c : t.getUniqueColumns()) {
      b
        .append("\n\n")
        .append(iSelectByUniqueTmpl
          .replace("{columncamel}", c.getCamelName())
          .replace("{columnpascal}", c.getPascalName()));
    }
    return b.toString();
  }

  private String generateSevice(Table t) {
    if (t.isJoined() || t.isLooped()) {
      return joinedClassTmpl
        .replace("{secondarytablepascal}", t.getSecondaryColumn().getForeignTable().getPascalName())
        .replace("{secondarytablecamel}", t.getSecondaryColumn().getForeignTable().getCamelName())
        .replace("{primarycolumncamel}", t.getPrimaryColumn().getCamelName())
        .replace("{secondarycolumncamel}", t.getSecondaryColumn().getCamelName())
        .replace("{tablepascal}", t.getPascalName())
        .replace("{rootname}", db.getProjectPath());
    } else {
      return classTmpl
        .replace("{primarycamel}", t.getPrimaryColumn().getCamelName())
        .replace("{selectbyunique}", generateSelectByUnique(t))
        .replace("{selectforparent}", generateSelectForParent(t))
        .replace("{tablepascal}", t.getPascalName())
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
          .replace("{parentpascal}", pt.getPascalName())
          .replace("{parentprimarycamel}", pt.getPrimaryColumn().getCamelName()));
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
          .replace("{columncamel}", c.getCamelName()));
    }
    return b.toString();
  }

  @Override
  protected void loadTemplates() {
    interfaceTmpl = loadTemplate("interface");
    iSelectByUniqueTmpl = loadTemplate("iselectbyunique");
    iSelectForParentTmpl = loadTemplate("iselectforparent");
    classTmpl = loadTemplate("class");
    selectByUniqueTmpl = loadTemplate("selectbyunique");
    selectForParentTmpl = loadTemplate(("selectforparent"));
    joinedClassTmpl = loadTemplate("joinedclass");
    joinedInterfaceTmpl = loadTemplate("joinedinterface");
  }
}
