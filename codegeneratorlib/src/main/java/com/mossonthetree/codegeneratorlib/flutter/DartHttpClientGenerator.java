package com.mossonthetree.codegeneratorlib.flutter;

import com.mossonthetree.codegeneratorlib.Column;
import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.Generator;
import com.mossonthetree.codegeneratorlib.Table;

import java.util.HashMap;
import java.util.Map;

public class DartHttpClientGenerator extends Generator {
  private String httpTmpl;
  private String httpClientJoinedTmpl;
  private String httpClientTmpl;
  private String selectForParentTmpl;
  private String selectByUniqueTmpl;

  public DartHttpClientGenerator(Database db) {
    super(db, "./templates/flutter/http");
  }

  @Override
  public Map<String, String> generate() {
    Map<String, String> files = new HashMap<>();
    files.put("http.dart", httpTmpl);
    for(Table t: db.getTables()) {
      files.put(t.getLowerName() + "_http_client.dart", generateHttpClient(t));
    }
    return files;
  }

  private String generateHttpClient(Table t) {
    if(t.isJoined() || t.isLooped()) {
      return httpClientJoinedTmpl
        .replace("{projectname}", db.getProjectName())
        .replace("{tablename}", t.getName())
        .replace("{primarytablename}", t.getPrimaryColumn().getForeignTable().getName())
        .replace("{parentpascal}", t.getPrimaryColumn().getForeignTable().getPascalName())
        .replace("{parentlower}", t.getPrimaryColumn().getForeignTable().getLowerName())
        .replace("{tablepascal}", t.getPascalName())
        .replace("{tablecamel}", t.getCamelName())
        .replace("{tablelower}", t.getLowerName())
        .replace("{primarycamel}", t.getPrimaryColumn().getCamelName())
        .replace("{secondarycamel}", t.getSecondaryColumn().getCamelName());
    } else {
      return httpClientTmpl
        .replace("{getforparent}", generateGetForParent(t))
        .replace("{selectbyunqiue}", generateSelectByUnique(t))
        .replace("{projectname}", db.getProjectName())
        .replace("{tablename}", t.getName())
        .replace("{tablepascal}", t.getPascalName())
        .replace("{tablelower}", t.getLowerName())
        .replace("{primarycamel}", t.getPrimaryColumn().getCamelName())
        .replace("{tablecamel}", t.getCamelName());
    }
  }

  private String generateGetForParent(Table t) {
    StringBuilder b = new StringBuilder();
    for(Table pt: t.getParentTables()) {
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
    for(Column col: t.getUniqueColumns()) {
      b
        .append("\n\n")
        .append(selectByUniqueTmpl
          .replace("{colpascal}", col.getPascalName())
          .replace("{colcamel}", col.getCamelName()));
    }
    return b.toString();
  }

  @Override
  protected void loadTemplates() {
    httpTmpl = loadTemplate("http");
    httpClientJoinedTmpl = loadTemplate("httpclassjoined");
    httpClientTmpl = loadTemplate("httpclass");
    selectForParentTmpl = loadTemplate("getforparent");
    selectByUniqueTmpl = loadTemplate("selectbyunique");
  }
}
