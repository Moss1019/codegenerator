package com.mossonthetree.codegeneratorlib.asp;

import com.mossonthetree.codegeneratorlib.Column;
import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.Generator;
import com.mossonthetree.codegeneratorlib.Table;

import java.util.*;

public class ViewGenerator extends Generator {
  private String classTmpl;
  private String fieldTmpl;
  private String childListTmpl;

  public ViewGenerator(Database db) {
    super(db, "./templates/dotnet/views");
  }

  public Map<String, String> generate() {
    Map<String, String> files = new HashMap<>();
    for (Table t : db.getTables()) {
      files.put(t.getPascalName() + "View.cs", generateView(t));
    }
    return files;
  }

  public String generateView(Table t) {
    return classTmpl
      .replace("{rootname}", db.getProjectPath())
      .replace("{tablepascal}", t.getPascalName())
      .replace("{fields}", generateFields(t))
      .replace("{childlists}", generateChildLists(t));
  }

  private String generateFields(Table t) {
    StringBuilder b = new StringBuilder();
    int i = 0;
    for (Column c : t.getColumns()) {
      if(i++ > 0) {
        b.append("\n");
      }
      b
        .append("\n\t\t")
        .append(fieldTmpl
          .replace("{datatype}", DataTypeUtil.resolvePrimitive(c))
          .replace("{columnpascal}", c.getPascalName())
          .replace("{datadefault}", DataTypeUtil.resolveDefault(c)));
    }
    return b.toString();
  }

  private String generateChildLists(Table t) {
    if (!t.hasLists()) {
      return "";
    }
    StringBuilder b = new StringBuilder();
    int i = 0;
    for (Table ct : t.getChildTables()) {
      if (ct.isLooped() || ct.isJoined()) {
        ++i;
        continue;
      }
      b
        .append("\n\n\t\t")
        .append(childListTmpl
          .replace("{childpascal}", ct.getPascalName()));
    }
    return b.toString();
  }

  @Override
  protected void loadTemplates() {
    classTmpl = loadTemplate("class");
    fieldTmpl = loadTemplate("field");
    childListTmpl = loadTemplate("childlist");
  }
}
