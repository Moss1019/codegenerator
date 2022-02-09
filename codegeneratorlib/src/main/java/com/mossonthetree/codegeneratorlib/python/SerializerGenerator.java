package com.mossonthetree.codegeneratorlib.python;

import com.mossonthetree.codegeneratorlib.Column;
import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.Generator;
import com.mossonthetree.codegeneratorlib.Table;

import java.util.HashMap;
import java.util.Map;

public class SerializerGenerator extends Generator {
  private String classTmpl;
  private String fieldEntryTmpl;
  private String serializerTmpl;

  public SerializerGenerator(Database db) {
    super(db, "./templates/python/serializers");
  }

  @Override
  public Map<String, String> generate() {
    Map<String, String> files = new HashMap<>();
    files.put("serializers.py", generateFile());
    return files;
  }

  private String generateFile() {
    return classTmpl
      .replace("{modelimports}", generateImports())
      .replace("{serializers}", generateSerializer())
      .replace("{fields}", generateFieldEntries());
  }

  private String generateImports() {
    StringBuilder b = new StringBuilder();
    int index = 0;
    for(Table t: db.getTables()) {
      b.append(t.getPascalName());
      if(index++ < db.getTables().size() - 1) {
        b.append(", ");
      }
    }
    return b.toString();
  }

  private String generateSerializer() {
    StringBuilder b = new StringBuilder();
    for(Table t: db.getTables()) {
      b
        .append("\n\n\n")
        .append(serializerTmpl
          .replace("{tablepascal}", t.getPascalName())
          .replace("{childlists}", generateChildLists(t)));
    }
    return b.toString();
  }

  private String generateFieldEntries() {
    StringBuilder b = new StringBuilder();
    for (Table t : db.getTables()) {
      b
        .append("\n")
        .append(fieldEntryTmpl
          .replace("{tablepascal}", t.getPascalName())
          .replace("{fields}", generateFields(t)));
    }
    return b.toString();
  }

  private String generateChildLists(Table t) {
    StringBuilder b = new StringBuilder();
    int index = 0;
    for(Table ct: t.getNonJoinedNonLoopedTables()) {
      b.append("'").append(ct.getName()).append("s").append("'");
      if(index++ < t.getNonJoinedNonLoopedTables().size() - 1) {
        b.append(", ");
      }
    }
    return b.toString();
  }

  private String generateFields(Table t) {
    StringBuilder b = new StringBuilder();
    int index = 0;
    for(Column c: t.getColumns()) {
      if(c.isForeign()) {
        b.append("'owner_").append(c.getName()).append("'");
      } else {
        b.append("'").append(c.getName()).append("'");
      }
      if(index++ < t.getColumns().size() - 1) {
        b.append(", ");
      }
    }
    return b.toString();
  }

  @Override
  protected void loadTemplates() {
    classTmpl = loadTemplate("class");
    fieldEntryTmpl = loadTemplate("fieldentry");
    serializerTmpl = loadTemplate("serializer");
  }
}
