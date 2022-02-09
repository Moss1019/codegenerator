package com.mossonthetree.codegeneratorlib.python;

import com.mossonthetree.codegeneratorlib.Column;
import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.Generator;
import com.mossonthetree.codegeneratorlib.Table;

import java.util.HashMap;
import java.util.Map;

public class UrlGenerator extends Generator {
  private String classTmpl;
  private String handleModelTmpl;
  private String handleModelsTmpl;
  private String getForParentTmpl;
  private String joinedClassTmpl;
  private String selectByUniqueTmpl;

  public UrlGenerator(Database db) {
    super(db, "./templates/python/urls");
  }

  @Override
  public Map<String, String> generate() {
    Map<String, String> files = new HashMap<>();
    files.put("urls.py", generateFile());
    return files;
  }

  private String generateFile() {
    return classTmpl
      .replace("{viewimports}", generateViewImports())
      .replace("{handlemodels}", generateHandleModels())
      .replace("{handlemodel}", generateHandleModel())
      .replace("{getforparents}", generateGetForParent())
      .replace("{getbyuniques}", generateSelectByUnique())
      .replace("{joinedclasses}", generateJoinedClassUrls());
  }

  private String generateViewImports() {
    StringBuilder b = new StringBuilder();
    int index = 0;
    for (Table t : db.getTables()) {
      b.append(t.getName()).append("_views");
      if (index++ < db.getTables().size() - 1) {
        b.append(", ");
      }
    }
    return b.toString();
  }

  private String generateHandleModels() {
    StringBuilder b = new StringBuilder();
    for (Table t : db.getTables()) {
      if (!t.isLooped() && !t.isJoined()) {
        b
          .append("\n")
          .append(handleModelsTmpl
            .replace("{tablename}", t.getName()));
      }
    }
    return b.toString();
  }

  private String generateHandleModel() {
    StringBuilder b = new StringBuilder();
    for (Table t : db.getTables()) {
      if (!t.isLooped() && !t.isJoined()) {
        b
          .append("\n")
          .append(handleModelTmpl
            .replace("{primaryname}", t.getPrimaryColumn().getName())
            .replace("{regextype}", DataTypeUtil.getRegexType(t.getPrimaryColumn()))
            .replace("{tablename}", t.getName()));
      }
    }
    return b.toString();
  }

  private String generateGetForParent() {
    StringBuilder b = new StringBuilder();
    for (Table t : db.getTables()) {
      if(!t.isJoined() && !t.isLooped()) {
        for (Table pt : t.getParentTables()) {
          b
            .append("\n")
            .append(getForParentTmpl
              .replace("{parenttablename}", pt.getName())
              .replace("{parentprimary}", pt.getPrimaryColumn().getName())
              .replace("{regextype}", DataTypeUtil.getRegexType(pt.getPrimaryColumn()))
              .replace("{tablename}", t.getName()));
        }
      }
    }
    return b.toString();
  }

  private String generateSelectByUnique() {
    StringBuilder b = new StringBuilder();
    for(Table t: db.getTables()) {
      for(Column c: t.getUniqueColumns()) {
        b
          .append("\n")
          .append(selectByUniqueTmpl
            .replace("{tablename}", t.getName())
            .replace("{colname}", c.getName())
            .replace("{regextype}", DataTypeUtil.getRegexType(c)))
          .append("\n");
      }
    }
    return b.toString();
  }

  private String generateJoinedClassUrls() {
    StringBuilder b = new StringBuilder();
    for(Table t: db.getTables()) {
      if(t.isJoined() || t.isLooped()) {
        b
          .append("\n")
          .append(joinedClassTmpl
            .replace("{tablelower}", t.getLowerName())
            .replace("{tablename}", t.getName())
            .replace("{primaryname}", t.getPrimaryColumn().getForeignTable().getName())
            .replace("{secondaryname}", t.getSecondaryColumn().getName())
            .replace("{primaryregextype}", DataTypeUtil.getRegexType(t.getPrimaryColumn()))
            .replace("{secondaryregextype}", DataTypeUtil.getRegexType(t.getSecondaryColumn())));
      }
    }
    return b.toString();
  }

  @Override
  protected void loadTemplates() {
    classTmpl = loadTemplate("class");
    handleModelTmpl = loadTemplate("handlemodel");
    handleModelsTmpl = loadTemplate("handlemodels");
    getForParentTmpl = loadTemplate("getforparent");
    joinedClassTmpl = loadTemplate("joinedclass");
    selectByUniqueTmpl = loadTemplate("getbyunique");
  }
}
