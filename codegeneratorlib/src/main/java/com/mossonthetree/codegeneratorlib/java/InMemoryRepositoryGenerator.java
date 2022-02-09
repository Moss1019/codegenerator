package com.mossonthetree.codegeneratorlib.java;

import com.mossonthetree.codegeneratorlib.Column;
import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.Generator;
import com.mossonthetree.codegeneratorlib.Table;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepositoryGenerator extends Generator {
  private String classTmpl;
  private String deleteChildrenTmpl;
  private String childRepoTmpl;
  private String selectByUniqueTmpl;
  private String selectOfParentTmpl;
  private String setChildListTmpl;
  private String joinedClassTmpl;
  private String baseRepoTmpl;

  public InMemoryRepositoryGenerator(Database db) {
    super(db, "./templates/java/inmemdb");
  }

  public Map<String, String> generate() {
    Map<String, String> files = new HashMap<>();
    files.put("BaseRepository.java", baseRepoTmpl.replace("{rootname}", db.getProjectPath()));
    for (Table t : db.getTables()) {
      files.put(t.getPascalName() + "Repository.java", generateRepository(t));
    }
    return files;
  }

  public String generateRepository(Table t) {
    if (t.isLooped() || t.isJoined()) {
      return joinedClassTmpl
        .replace("{rootname}", db.getProjectPath())
        .replace("{tablepascal}", t.getPascalName())
        .replace("{primarycamel}", t.getPrimaryColumn().getForeignTable().getCamelName())
        .replace("{secondarycamel}", t.getSecondaryColumn().getForeignTable().getCamelName())
        .replace("{tablecamel}", t.getCamelName())
        .replace("{primarycolcamel}", t.getPrimaryColumn().getCamelName())
        .replace("{secondarycolcamel}", t.getSecondaryColumn().getCamelName())
        .replace("{primarytablepascal}", t.getPrimaryColumn().getForeignTable().getPascalName());
    } else {
      return classTmpl
        .replace("{deletechildren}", generateDeleteChildren(t))
        .replace("{primarycamel}", t.getPrimaryColumn().getCamelName())
        .replace("{selectbyunique}", generateSelectByUnique(t))
        .replace("{selectofparent}", generateSelectOfParent(t))
        .replace("{childrepos}", generateChildRepos(t))
        .replace("{setchildlists}", generateSetChildLists(t))
        .replace("{tablepascal}", t.getPascalName())
        .replace("{tablecamel}", t.getCamelName())
        .replace("{primarydatatype}", DataTypeUtil.resolvePrimitive(t.getPrimaryColumn()))
        .replace("{rootname}", db.getProjectPath());
    }
  }

  private String generateDeleteChildren(Table t) {
    StringBuilder b = new StringBuilder();
    for (Table ct : t.getChildTables()) {
      if (ct.isLooped() || ct.isJoined()) {
        continue;
      }
      b
        .append("\n")
        .append(deleteChildrenTmpl
          .replace("{childtablepascal}", ct.getPascalName())
          .replace("{childtablecamel}", ct.getCamelName()));
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
          .replace("{columnpascal}", c.getPascalName()));
      if (++i < t.getUniqueColumns().size()) {
        b.append("\n\n");
      }
    }
    return b.toString();
  }

  private String generateSelectOfParent(Table t) {
    StringBuilder b = new StringBuilder();
    int i = 0;
    for (Table pt : t.getParentTables()) {
      b
        .append("\n\n")
        .append(selectOfParentTmpl
          .replace("{parentpascal}", pt.getPascalName())
          .replace("{parentprimarydatatype}", DataTypeUtil.resolvePrimitive(pt.getPrimaryColumn()))
          .replace("{parentprimarycamel}", pt.getPrimaryColumn().getCamelName())
          .replace("{parentprimarypascal}", pt.getPrimaryColumn().getPascalName()));
      if (++i < t.getParentTables().size()) {
        b.append("\n\n");
      }
    }
    return b.toString();
  }

  private String generateChildRepos(Table t) {
    StringBuilder b = new StringBuilder();
    for (Table ct : t.getChildTables()) {
      if (ct.isJoined() || ct.isLooped()) {
        continue;
      }
      b
        .append("\n")
        .append(childRepoTmpl
          .replace("{childtablepascal}", ct.getPascalName())
          .replace("{childtablecamel}", ct.getCamelName()));
    }
    return b.toString();
  }

  private String generateSetChildLists(Table t) {
    StringBuilder b = new StringBuilder();
    for (Table ct : t.getChildTables()) {
      if (ct.isLooped() || ct.isJoined()) {
        continue;
      }
      b
        .append("\n")
        .append(setChildListTmpl
          .replace("{childtablepascal}", ct.getPascalName())
          .replace("{childtablecamel}", ct.getCamelName()));
    }
    return b.toString();
  }

  @Override
  protected void loadTemplates() {
    classTmpl = loadTemplate("class");
    deleteChildrenTmpl = loadTemplate("deletechildren");
    childRepoTmpl = loadTemplate("childrepo");
    selectByUniqueTmpl = loadTemplate("selectbyunique");
    selectOfParentTmpl = loadTemplate("selectofparent");
    setChildListTmpl = loadTemplate("setchildlist");
    joinedClassTmpl = loadTemplate("joinedclass");
    baseRepoTmpl = loadTemplate("baserepo");
  }
}
