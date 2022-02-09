package com.mossonthetree.codegeneratorlib.elasticsearch;

import com.mossonthetree.codegeneratorlib.Column;
import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.Generator;
import com.mossonthetree.codegeneratorlib.Table;

import java.util.HashMap;
import java.util.Map;

public class JavaElasticClientGenerator extends Generator {
  private String classTmpl;
  private String guidPrimaryFieldStringTmpl;
  private String guidPrimaryStringTmpl;
  private String intPrimaryFieldStringTmpl;
  private String intPrimaryStringTmpl;
  private String selectByUniqueTmpl;

  public JavaElasticClientGenerator(Database db) {
    super(db, "./templates/elasticsearch/java");
  }

  @Override
  public Map<String, String> generate() {
    Map<String, String> files = new HashMap<>();
    for(Table t: db.getTables()) {
      if(t.isLooped() || t.isJoined()) {

      } else {
        files.put(t.getPascalName() + "ViewElasticClient.java", generateClient(t));
      }
    }
    return files;
  }

  private String generateClient(Table t) {
    return classTmpl
      .replace("{primaryfieldstring}", generatePrimaryFieldString(t))
      .replace("{primarystring}", generatePrimaryString(t))
      .replace("{selectbyunique}", generateSelectByUnique(t))
      .replace("{tablepascal}", t.getPascalName())
      .replace("{tablecamel}", t.getCamelName())
      .replace("{primarycamel}", t.getPrimaryColumn().getCamelName())
      .replace("{primarypascal}", t.getPrimaryColumn().getPascalName())
      .replace("{primarydatatype}", DataTypeUtil.resolvePrimitive(t.getPrimaryColumn()))
      .replace("{rootname}", db.getProjectPath());
  }

  private String generatePrimaryFieldString(Table t) {
    switch(t.getPrimaryColumn().getDataType()) {
      case "int":
        return intPrimaryFieldStringTmpl;
      case "guid":
        return guidPrimaryFieldStringTmpl;
    }
    return "";
  }

  private String generatePrimaryString(Table t) {
    switch(t.getPrimaryColumn().getDataType()) {
      case "int":
        return intPrimaryStringTmpl;
      case "guid":
        return guidPrimaryStringTmpl;
    }
    return "";
  }

  private String generateSelectByUnique(Table t) {
    StringBuilder b = new StringBuilder();
    for(Column c: t.getUniqueColumns()) {
      b
        .append("\n\n")
        .append(selectByUniqueTmpl
          .replace("{columncamel}", c.getCamelName())
          .replace("{columnpascal}", c.getPascalName()));
    }
    return b.toString();
  }

  @Override
  protected void loadTemplates() {
    classTmpl = loadTemplate("class");
    guidPrimaryFieldStringTmpl = loadTemplate("guidfieldstring");
    guidPrimaryStringTmpl = loadTemplate("guidstring");
    intPrimaryFieldStringTmpl = loadTemplate("integerfieldstring");
    intPrimaryStringTmpl = loadTemplate("integerstring");
    selectByUniqueTmpl = loadTemplate("selectbyunique");
  }
}
