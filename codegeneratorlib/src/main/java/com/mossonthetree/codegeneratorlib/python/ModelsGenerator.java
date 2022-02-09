package com.mossonthetree.codegeneratorlib.python;

import com.mossonthetree.codegeneratorlib.Column;
import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.Generator;
import com.mossonthetree.codegeneratorlib.Table;

import java.util.HashMap;
import java.util.Map;

public class ModelsGenerator extends Generator {
  private String classTmpl;
  private String joinedClassTmpl;
  private String fieldTmpl;
  private String modelTmpl;

  public ModelsGenerator(Database db) {
    super(db, "./templates/python/models");
  }

  @Override
  public Map<String, String> generate() {
    Map<String, String> files = new HashMap<>();
    files.put("models.py", generateModels());
    return files;
  }

  private String generateModels() {
    StringBuilder b = new StringBuilder();
    for (Table t : db.getTables()) {
      if (t.isLooped() || t.isJoined()) {
        b
          .append("\n\n\n")
          .append(joinedClassTmpl
            .replace("{tablepascal}", t.getPascalName())
            .replace("{primaryname}", t.getPrimaryColumn().getForeignTable().getName())
            .replace("{primarypascal}", t.getPrimaryColumn().getForeignTable().getPascalName())
            .replace("{secondaryname}", t.getSecondaryColumn().getForeignTable().getName())
            .replace("{secondarypascal}", t.getSecondaryColumn().getForeignTable().getPascalName()));
      } else {
        b
          .append("\n\n\n")
          .append(modelTmpl
            .replace("{fields}", generateFields(t))
            .replace("{tablepascal}", t.getPascalName()));
      }
    }
    return classTmpl
      .replace("{models}", b);
  }

  private String generateFields(Table t) {
    StringBuilder b = new StringBuilder();
    for (Column c : t.getColumns()) {
      if (c.isForeign()) {
        b
          .append("\n")
          .append(fieldTmpl
            .replace("{colname}", c.getForeignTableName())
            .replace("{datatype}", "ForeignKey")
            .replace("{options}", generateForeignOptions(c)));
      } else {
        b
          .append("\n")
          .append(fieldTmpl
            .replace("{colname}", c.getName())
            .replace("{datatype}", DataTypeUtil.resolveModelType(c))
            .replace("{options}", generateOptions(c)));
      }
    }
    return b.toString();
  }

  private String generateForeignOptions(Column c) {
    return String.format("%s, on_delete=models.CASCADE", c.getForeignTable().getPascalName());
  }

  private String generateOptions(Column c) {
    boolean needComma = false;
    StringBuilder b = new StringBuilder();
    b.append(DataTypeUtil.resolveBasicOption(c));
    needComma = b.length() > 0;
    if (c.isPrimary()) {
      b
        .append(needComma ? ", " : "")
        .append("primary_key=True");
      needComma = true;
    }
    if(c.isUnique()) {
      b
        .append(needComma ? ", " : "")
        .append("unique=True");
    }
    return b.toString();
  }

  @Override
  protected void loadTemplates() {
    classTmpl = loadTemplate("class");
    joinedClassTmpl = loadTemplate("joinedclass");
    fieldTmpl = loadTemplate("field");
    modelTmpl = loadTemplate("model");
  }
}
