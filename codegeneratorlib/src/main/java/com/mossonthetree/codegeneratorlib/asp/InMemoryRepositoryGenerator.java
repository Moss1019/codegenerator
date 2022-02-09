package com.mossonthetree.codegeneratorlib.asp;

import com.mossonthetree.codegeneratorlib.*;

import java.util.*;

public class InMemoryRepositoryGenerator extends Generator {
  private String interfaceTmpl;
  private String iSelectByUniqueTmpl;
  private String iSelectForParentTmpl;
  private String joinedInterfaceTmpl;
  private String classTmpl;
  private String deleteChildrenTmpl;
  private String selectByUniqueTmpl;
  private String selectForParentTmpl;
  private String joinedClassTmpl;
  private String childListTmpl;
  private String baseRepoTmpl;
  private String dbContextTmpl;
  private String childRepoTmpl;
  private String childRepoArgTmpl;
  private String childRepoAssignTmpl;

  public InMemoryRepositoryGenerator(Database db) {
    super(db, "./templates/dotnet/" + (db.getDatabaseType() == DatabaseType.InMemory ? "inmemrepositories" :
      db.getDatabaseType() == DatabaseType.Sql ? "sqlrepositories" :
        "documentrepositories"));
  }

  public Map<String, String> generate() {
    Map<String, String> files = new HashMap<>();
    files.put("BaseRepository.cs", baseRepoTmpl.replace("{rootname}", db.getProjectPath()));
    files.put("DbContext.cs", dbContextTmpl.replace("{rootname}", db.getProjectPath()));
    for (Table t : db.getTables()) {
      files.put("I" + t.getPascalName() + "Repository.cs", generateInterface(t));
      files.put(t.getPascalName() + "Repository.cs", generateRepository(t));
    }
    return files;
  }

  private String generateInterface(Table t) {
    if (t.isLooped() || t.isJoined()) {
      return joinedInterfaceTmpl
        .replace("{rootname}", db.getProjectPath())
        .replace("{tablepascal}", t.getPascalName())
        .replace("{primarycolumncamel}", t.getPrimaryColumn().getCamelName())
        .replace("{secondarycolumncamel}", t.getSecondaryColumn().getOwnerTable().getCamelName())
        .replace("{secondarytablepascal}", t.getSecondaryColumn().getForeignTable().getPascalName());
    } else {
      return interfaceTmpl
        .replace("{primarycolumncamel}", t.getPrimaryColumn().getCamelName())
        .replace("{selectbyunique}", generateInterfaceSelectByUnique(t))
        .replace("{selectforparent}", generateInterfaceSelectForParent(t))
        .replace("{tablepascal}", t.getPascalName())
        .replace("{rootname}", db.getProjectPath());
    }
  }

  private String generateInterfaceSelectByUnique(Table t) {
    StringBuilder b = new StringBuilder();
    int i = 0;
    for (Column c : t.getUniqueColumns()) {
      b
        .append("\n\n")
        .append(iSelectByUniqueTmpl
          .replace("{columnpascal}", c.getPascalName())
          .replace("{columncamel}", c.getCamelName()));
    }
    return b.toString();
  }

  private String generateInterfaceSelectForParent(Table t) {
    StringBuilder b = new StringBuilder();
    int i = 0;
    for (Table pt : t.getParentTables()) {
      b
        .append("\n\n")
        .append(iSelectForParentTmpl
          .replace("{parentpascal}", pt.getPascalName())
          .replace("{parentprimarycamel}", pt.getPrimaryColumn().getCamelName()));
    }
    return b.toString();
  }

  private String generateRepository(Table t) {
    if (t.isLooped() || t.isJoined()) {
      return joinedClassTmpl
        .replace("{rootname}", db.getProjectPath())
        .replace("{tablepascal}", t.getPascalName())
        .replace("{primarycolumncamel}", t.getPrimaryColumn().getCamelName())
        .replace("{secondarytablepascal}", t.getSecondaryColumn().getForeignTable().getPascalName())
        .replace("{secondarycolumncamel}", t.getSecondaryColumn().getCamelName())
        .replace("{primarytablepascal}", t.getPrimaryColumn().getForeignTable().getPascalName())
        .replace("{primarytablecamel}", t.getPrimaryColumn().getForeignTable().getCamelName())
        .replace("{primarycolumnpascal}", t.getPrimaryColumn().getPascalName())
        .replace("{secondarycolumnpascal}", t.getSecondaryColumn().getPascalName())
        .replace("{secondarytablelower}", t.getSecondaryColumn().getForeignTable().getLowerName())
        .replace("{joinedcolumnpascal}", t.getSecondaryColumn().getForeignTable().getPrimaryColumn().getPascalName());
    } else {
      return classTmpl
        .replace("{primarycolumncamel}", t.getPrimaryColumn().getCamelName())
        .replace("{primarycolumnpascal}", t.getPrimaryColumn().getPascalName())
        .replace("{childlists}", generateChildlist(t))
        .replace("{selectbyunique}", generateSelectByUnique(t))
        .replace("{selectforparent}", generateSelectForParent(t))
        .replace("{childrepos}", generateChildRepos(t))
        .replace("{childrepoargs}", generateChildRepoArgs(t))
        .replace("{childrepoassigns}", generateChildRepoAssigns(t))
        .replace("{deletechildren}", generateDeleteChildren(t))
        .replace("{tablepascal}", t.getPascalName())
        .replace("{tablelower}", t.getLowerName())
        .replace("{rootname}", db.getProjectPath());
    }
  }

  private String generateSelectByUnique(Table t) {
    StringBuilder b = new StringBuilder();
    int i = 0;
    for (Column c : t.getUniqueColumns()) {
      b
        .append("\n\n")
        .append(selectByUniqueTmpl
          .replace("{columnpascal}", c.getPascalName())
          .replace("{columncamel}", c.getCamelName())
          .replace("{childlists}", generateChildlist(t)));
    }
    return b.toString();
  }

  private String generateChildlist(Table t) {
    StringBuilder b = new StringBuilder();
    for (Table ct : t.getChildTables()) {
      if (ct.isLooped() || ct.isJoined()) {
        continue;
      }
      b
        .append("\n")
        .append(childListTmpl
          .replace("{childtablepascal}", ct.getPascalName())
          .replace("{primarycolumnpascal}", t.getPrimaryColumn().getPascalName())
          .replace("{childtablecamel}", ct.getCamelName()));
    }
    return b.toString();
  }

  private String generateSelectForParent(Table t) {
    StringBuilder b = new StringBuilder();
    int i = 0;
    for (Table pt : t.getParentTables()) {
      b
        .append("\n\n")
        .append(selectForParentTmpl
          .replace("{parentprimarycamel}", pt.getPrimaryColumn().getCamelName())
          .replace("{parentprimarypascal}", pt.getPrimaryColumn().getPascalName())
          .replace("{parentpascal}", pt.getPascalName())
          .replace("{childlists}", generateChildlist(t)));
    }
    return b.toString();
  }

  private String generateDeleteChildren(Table t) {
    StringBuilder b = new StringBuilder();
    for (Table ct : t.getChildTables()) {
      if (ct.isJoined() || ct.isLooped()) {
        continue;
      }
      b
        .append("\n")
        .append(deleteChildrenTmpl
          .replace("{childtablepascal}", ct.getPascalName())
          .replace("{childtablecamel}", ct.getCamelName())
          .replace("{childprimarypascal}", ct.getPrimaryColumn().getPascalName()));
    }
    return b.toString();
  }

  private String generateChildRepos(Table t) {
    StringBuilder b = new StringBuilder();
    for (Table ct : t.getChildTables()) {
      b.append(childRepoTmpl
        .replace("{childtablepascal}", ct.getPascalName())
        .replace("{childtablecamel}", ct.getCamelName()))
        .append("\n");
    }
    return b.toString();
  }

  private String generateChildRepoArgs(Table t) {
    StringBuilder b = new StringBuilder();
    for (Table ct : t.getChildTables()) {
      b.append(childRepoArgTmpl
        .replace("{childtablepascal}", ct.getPascalName())
        .replace("{childtablecamel}", ct.getCamelName()));
    }
    return b.toString();
  }

  private String generateChildRepoAssigns(Table t) {
    StringBuilder b = new StringBuilder();
    for (Table ct : t.getChildTables()) {
      b
        .append("\n\t\t\t")
        .append(childRepoAssignTmpl
          .replace("{childtablecamel}", ct.getCamelName()));
    }
    return b.toString();
  }

  @Override
  protected void loadTemplates() {
    interfaceTmpl = loadTemplate("interface");
    iSelectByUniqueTmpl = loadTemplate("iselectbyunique");
    iSelectForParentTmpl = loadTemplate("iselectforparent");
    classTmpl = loadTemplate("class");
    deleteChildrenTmpl = loadTemplate("deletechildren");
    selectByUniqueTmpl = loadTemplate("selectbyunique");
    selectForParentTmpl = loadTemplate("selectforparent");
    joinedInterfaceTmpl = loadTemplate("joinedinterface");
    joinedClassTmpl = loadTemplate("joinedclass");
    childListTmpl = loadTemplate("childlist");
    baseRepoTmpl = loadTemplate("baserepo");
    dbContextTmpl = loadTemplate("dbcontext");
    childRepoTmpl = loadTemplate("childrepo");
    childRepoArgTmpl = loadTemplate("childrepoarg");
    childRepoAssignTmpl = loadTemplate("childrepoassign");
  }
}
 
