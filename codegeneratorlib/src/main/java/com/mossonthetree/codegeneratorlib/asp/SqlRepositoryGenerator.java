package com.mossonthetree.codegeneratorlib.asp;

import com.mossonthetree.codegeneratorlib.Column;
import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.Generator;
import com.mossonthetree.codegeneratorlib.Table;

import java.util.*;

public class SqlRepositoryGenerator extends Generator {
  private String interfaceTmpl;
  private String iSelectByUniqueTmpl;
  private String iSelectForParentTmpl;
  private String joinedInterfaceTmpl;
  private String classTmpl;
  private String selectByUniqueTmpl;
  private String selectForParentTmpl;
  private String includeChildTmpl;
  private String joinedClassTmpl;
  private String contextTmpl;
  private String contextCompositeTmpl;
  private String contextSetTmpl;

  public SqlRepositoryGenerator(Database db) {
    super(db, "./templates/dotnet/entityframework");
  }

  public Map<String, String> generate() {
    Map<String, String> files = new HashMap<>();
    files.put(db.getProjectPath() + "Context.cs", generateContext());
    for (Table t : db.getTables()) {
      files.put("I" + t.getPascalName() + "Repository.cs", generateInterface(t));
      files.put(t.getPascalName() + "Repository.cs", generateClass(t));
    }
    return files;
  }

  private String generateContext() {
    StringBuilder b = new StringBuilder();
    StringBuilder k = new StringBuilder();
    for (Table t : db.getTables()) {
      b
        .append("\n\n\t\t")
        .append(contextSetTmpl
          .replace("{tablepascal}", t.getPascalName()));
      if (t.isJoined() || t.isLooped()) {
        k
          .append("\n\t\t\t")
          .append(contextCompositeTmpl
            .replace("{tablepascal}", t.getPascalName())
            .replace("{primarycolpascal}", t.getPrimaryColumn().getPascalName())
            .replace("{secondarycolpascal}", t.getSecondaryColumn().getPascalName()));
      }
    }
    return contextTmpl
      .replace("{sets}", b.toString())
      .replace("{composites}", k.toString())
      .replace("{rootname}", db.getProjectPath());
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

  private String generateClass(Table t) {
    if (t.isLooped() || t.isJoined()) {
      return joinedClassTmpl
        .replace("{rootname}", db.getProjectPath())
        .replace("{tablepascal}", t.getPascalName())
        .replace("{primarycamel}", t.getPrimaryColumn().getCamelName())
        .replace("{secondarycamel}", t.getSecondaryColumn().getCamelName())
        .replace("{primarypascal}", t.getPrimaryColumn().getPascalName())
        .replace("{secondarypascal}", t.getSecondaryColumn().getPascalName())
        .replace("{secondarytablepascal}", t.getLoopedJoinedPascal())
        .replace("{resultpascal}", t.getSecondaryColumn().getForeignTable().getPascalName());
    } else {
      return classTmpl
        .replace("{primarycamel}", t.getPrimaryColumn().getCamelName())
        .replace("{primarypascal}", t.getPrimaryColumn().getPascalName())
        .replace("{selectbyunique}", generateSelectByUnique(t))
        .replace("{selectforparent}", generateSelectForParent(t))
        .replace("{childlists}", generateChildLists(t))
        .replace("{tablepascal}", t.getPascalName())
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
          .replace("{columncamel}", c.getCamelName()));
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
        .replace("{parentpascal}", pt.getPascalName())
        .replace("{primarypascal}", pt.getPrimaryColumn().getPascalName())
        .replace("{primarycamel}", pt.getPrimaryColumn().getCamelName()));
    }
    return b.toString();
  }

  private String generateChildLists(Table t) {
    StringBuilder b = new StringBuilder();
    for (Table ct : t.getChildTables()) {
      if (ct.isJoined() || ct.isLooped()) {
        continue;
      }
      b
        .append("\n\t\t\t\t\t")
        .append(includeChildTmpl
          .replace("{childtablepascal}", ct.getPascalName()));
    }
    return b.toString();
  }

  @Override
  protected void loadTemplates() {
    interfaceTmpl = loadTemplate("interface");
    iSelectByUniqueTmpl = loadTemplate("iselectbyunique");
    iSelectForParentTmpl = loadTemplate("iselectforparent");
    joinedInterfaceTmpl = loadTemplate("joinedinterface");
    classTmpl = loadTemplate("class");
    selectByUniqueTmpl = loadTemplate("selectbyunique");
    selectForParentTmpl = loadTemplate("selectforparent");
    includeChildTmpl = loadTemplate("includechild");
    joinedClassTmpl = loadTemplate("joinedclass");
    contextTmpl = loadTemplate("context");
    contextCompositeTmpl = loadTemplate("contextcomposite");
    contextSetTmpl = loadTemplate("contextset");
  }
}
