package com.mossonthetree.codegeneratorlib.java;

import com.mossonthetree.codegeneratorlib.Column;
import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.Generator;
import com.mossonthetree.codegeneratorlib.Table;

import java.util.HashMap;
import java.util.Map;

public class ServiceGenerator extends Generator {
  private String classTmpl;
  private String selectByUniqueTmpl;
  private String selectForParentTmpl;
  private String setGuidPrimaryTmpl;
  private String joiningClassTmpl;

  public ServiceGenerator(Database db) {
    super(db, "./templates/java/services");
  }

  public Map<String, String> generate() {
    Map<String, String> files = new HashMap<>();
    for (Table t : db.getTables()) {
      files.put(t.getPascalName() + "Service.java", generateService(t));
    }
    return files;
  }

  private String generateService(Table t) {
    if (t.isJoined() || t.isLooped()) {
      return joiningClassTmpl
        .replace("{primarycamel}", t.getPrimaryColumn().getCamelName())
        .replace("{secondarycamel}", t.getSecondaryColumn().getCamelName())
        .replace("{primarydatatype}", DataTypeUtil.resolvePrimitive(t.getPrimaryColumn()))
        .replace("{secondarydatatype}", DataTypeUtil.resolvePrimitive(t.getSecondaryColumn()))
        .replace("{primarytablepascal}", t.getPrimaryColumn().getForeignTable().getPascalName())
        .replace("{secondarytablepascal}", t.getSecondaryColumn().getForeignTable().getPascalName())
        .replace("{tablepascal}", t.getPascalName())
        .replace("{rootname}", db.getProjectPath());
    } else {
      return classTmpl
        .replace("{setprimary}", generateSetPrimary(t))
        .replace("{selectbyunique}", generateSelectByUnique(t))
        .replace("{selectforparent}", generateSelectForParent(t))
        .replace("{tablepascal}", t.getPascalName())
        .replace("{tablecamel}", t.getCamelName())
        .replace("{primarypascal}", t.getPrimaryColumn().getPascalName())
        .replace("{primarydatatype}", DataTypeUtil.resolvePrimitive(t.getPrimaryColumn()))
        .replace("{rootname}", db.getProjectPath());
    }
  }

  private String generateSetPrimary(Table t) {
    switch(t.getPrimaryColumn().getDataType()) {
      case "guid":
        return setGuidPrimaryTmpl;
      default:
        return "";
    }
  }

  private String generateSelectByUnique(Table t) {
    StringBuilder b = new StringBuilder();
    int i = 0;
    for (Column c : t.getUniqueColumns()) {
      b
        .append("\n\n\t")
        .append(selectByUniqueTmpl
          .replace("{coldatatype}", DataTypeUtil.resolvePrimitive(c))
          .replace("{columnpascal}", c.getPascalName()));
      if (++i < t.getUniqueColumns().size()) {
        b.append("\n\n");
      }
    }
    return b.toString();
  }

  private String generateSelectForParent(Table t) {
    StringBuilder b = new StringBuilder();
    int i = 0;
    for (Table pt : t.getParentTables()) {
      b
        .append("\n\n\t")
        .append(selectForParentTmpl
          .replace("{parentpascal}", pt.getPascalName())
          .replace("{parentprimarydatatype}", DataTypeUtil.resolvePrimitive(pt.getPrimaryColumn()))
          .replace("{parentprimarycamel}", pt.getPrimaryColumn().getCamelName()));
      if (++i < t.getParentTables().size()) {
        b.append("\n\n");
      }
    }
    return b.toString();
  }

  @Override
  protected void loadTemplates() {
    classTmpl = loadTemplate("class");
    selectByUniqueTmpl = loadTemplate("selectbyunique");
    selectForParentTmpl = loadTemplate("selectforparent");
    setGuidPrimaryTmpl = loadTemplate("setguid");
    joiningClassTmpl = loadTemplate("joiningclass");
  }
}

