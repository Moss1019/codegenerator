package com.mossonthetree.codegeneratorlib.java;

import com.mossonthetree.codegeneratorlib.Column;
import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.Generator;
import com.mossonthetree.codegeneratorlib.Table;

import java.util.HashMap;
import java.util.Map;

public class JpaEntityGenerator extends Generator {
  private String classTmpl;
  private String childListTmpl;
  private String fieldTmpl;
  private String parentObjTmpl;
  private String getChildTmpl;
  private String getterTmpl;
  private String setChildTmpl;
  private String setterTmpl;
  private String joinedClassTmpl;

  public JpaEntityGenerator(Database db) {
    super(db, "./templates/java/sqlentities");
  }

  @Override
  public Map<String, String> generate() {
    Map<String, String> files = new HashMap<>();
    for (Table t : db.getTables()) {
      files.put(t.getPascalName() + ".java", generateEntity(t));
    }
    return files;
  }

  private String generateEntity(Table t) {
    if (t.isJoined() || t.isLooped()) {
      return joinedClassTmpl
        .replace("{tablepascal}", t.getPascalName())
        .replace("{primarydatatype}", DataTypeUtil.resolvePrimitive(t.getPrimaryColumn()))
        .replace("{secondarydatatype}", DataTypeUtil.resolvePrimitive(t.getSecondaryColumn()))
        .replace("{primarycolcamel}", t.getPrimaryColumn().getCamelName())
        .replace("{secondarycolcamel}", t.getSecondaryColumn().getCamelName())
        .replace("{primarydefault}", DataTypeUtil.resolveDefault(t.getPrimaryColumn()))
        .replace("{secondarydefault}", DataTypeUtil.resolveDefault(t.getSecondaryColumn()))
        .replace("{primarycolpascal}", t.getPrimaryColumn().getPascalName())
        .replace("{secondarycolpascal}", t.getSecondaryColumn().getPascalName())
        .replace("{rootname}", db.getProjectPath());
    } else {
      return classTmpl
        .replace("{fields}", generateFields(t))
        .replace("{getters}", generateGetters(t))
        .replace("{setters}", generateSetters(t))
        .replace("{tablecamel}", t.getCamelName())
        .replace("{tablepascal}", t.getPascalName())
        .replace("{rootname}", db.getProjectPath());
    }
  }

  private String generateFields(Table t) {
    StringBuilder b = new StringBuilder();
    addFields(b, t);
    addParentObjs(b, t);
    addChildLists(b, t);
    return b.toString();
  }

  private void addFields(StringBuilder b, Table t) {
    for (Column c : t.getColumns()) {
      if (c.isForeign()) {
        continue;
      }
      StringBuilder optionsBuilder = new StringBuilder();
      if (c.isPrimary()) {
        optionsBuilder.append("@Id\n\t");
      }
      if (c.isAutoIncrement()) {
        optionsBuilder.append("@GeneratedValue\n\t");
      }
      b
        .append("\n")
        .append(fieldTmpl
          .replace("{options}", optionsBuilder.toString())
          .replace("{datatype}", DataTypeUtil.resolvePrimitive(c))
          .replace("{columncamel}", c.getCamelName())
          .replace("{default}", DataTypeUtil.resolveDefault(c)));
    }
  }

  private void addParentObjs(StringBuilder b, Table t) {
    for (Table pt : t.getParentTables()) {
      StringBuilder ig = new StringBuilder();
      int i = 0;
      for (Table ppt : pt.getParentTables()) {
        ig.append(ppt.getCamelName());
        if (++i < pt.getParentTables().size() || pt.getChildTables().size() > 0) {
          ig.append(" ");
        }
      }
      i = 0;
      for (Table ct : pt.getChildTables()) {
        ig.append(ct.getCamelName()).append("s");
        if (++i < pt.getChildTables().size()) {
          ig.append(" ");
        }
      }
      b
        .append("\n")
        .append(parentObjTmpl
          .replace("{default}", DataTypeUtil.resolveDefault(pt.getPrimaryColumn()))
          .replace("{parentprimarydatatype}", DataTypeUtil.resolvePrimitive(pt.getPrimaryColumn()))
          .replace("{parentprimarycamel}", pt.getPrimaryColumn().getCamelName())
          .replace("{ignorechildlists}", ig.toString())
          .replace("{parentpascal}", pt.getPascalName())
          .replace("{parentcamel}", pt.getCamelName()));
    }
  }

  private void addChildLists(StringBuilder b, Table t) {
    for (Table ct : t.getChildTables()) {
      if (ct.isLooped() || ct.isJoined()) {
        StringBuilder ig = new StringBuilder();
        Table primaryTable = ct.getPrimaryColumn().getForeignTable();
        Table secondaryTable = ct.getSecondaryColumn().getForeignTable();
        boolean isCurrentPrimary = primaryTable.getPascalName().equals(t.getPascalName());
        Table activeTable = isCurrentPrimary ? secondaryTable : primaryTable;
        int i = 0;
        for (Table pt : activeTable.getParentTables()) {
          ig.append(pt.getCamelName());
          if (++i < activeTable.getParentTables().size() || activeTable.getChildTables().size() > 0) {
            ig.append(" ");
          }
        }
        i = 0;
        for (Table cct : activeTable.getChildTables()) {
          ig.append(cct.getCamelName()).append("s");
          if (++i < activeTable.getChildTables().size()) {
            ig.append(" ");
          }
        }
        b
          .append("\n")
          .append(childListTmpl
            .replace("{mapping}", "")
            .replace("{childtablepascal}", isCurrentPrimary ? secondaryTable.getPascalName() : primaryTable.getPascalName())
            .replace("{childtablecamel}", ct.getCamelName())
            .replace("{ignorechildlists}", ig.toString()));
      } else {
        StringBuilder ig = new StringBuilder();
        int i = 0;
        for (Table pt : ct.getParentTables()) {
          ig.append(pt.getCamelName());
          if (++i < ct.getParentTables().size() || ct.getChildTables().size() > 0) {
            ig.append(" ");
          }
        }
        i = 0;
        for (Table cct : ct.getChildTables()) {
          ig.append(cct.getCamelName()).append("s");
          if (++i < ct.getChildTables().size()) {
            ig.append(" ");
          }
        }
        b
          .append("\n")
          .append(childListTmpl
            .replace("{mapping}", "(mappedBy = \"{mappedby}\")")
            .replace("{mappedby}", t.getCamelName())
            .replace("{ignorechildlists}", ig.toString())
            .replace("{childtablepascal}", ct.getPascalName())
            .replace("{childtablecamel}", ct.getCamelName()));
      }
    }
  }

  private String generateGetters(Table t) {
    StringBuilder b = new StringBuilder();
    for (Column c : t.getColumns()) {
      if (c.isForeign()) {
        continue;
      }
      b
        .append("\n\n")
        .append(getterTmpl
          .replace("{datatype}", DataTypeUtil.resolvePrimitive(c))
          .replace("{columnpascal}", c.getPascalName())
          .replace("{columncamel}", c.getCamelName()));
    }

    for (Table pt : t.getParentTables()) {
      b
        .append("\n\n")
        .append(getterTmpl
          .replace("{datatype}", DataTypeUtil.resolvePrimitive(pt.getPrimaryColumn()))
          .replace("{columnpascal}", pt.getPrimaryColumn().getPascalName())
          .replace("{columncamel}", pt.getPrimaryColumn().getCamelName()))
        .append("\n\n")
        .append(getterTmpl
          .replace("{datatype}", pt.getPascalName())
          .replace("{columnpascal}", pt.getPascalName())
          .replace("{columncamel}", pt.getCamelName()));
    }

    for (Table ct : t.getChildTables()) {
      if (ct.isLooped() || ct.isJoined()) {
        Table primaryTable = ct.getPrimaryColumn().getForeignTable();
        Table secondaryTable = ct.getSecondaryColumn().getForeignTable();
        Table active = t.getPascalName().equals(primaryTable.getPascalName()) ? secondaryTable : primaryTable;
        b
          .append("\n\n")
          .append(getChildTmpl
            .replace("{fieldpascal}", ct.getPascalName())
            .replace("{childtablepascal}", active.getPascalName())
            .replace("{childtablecamel}", ct.getCamelName()));
      } else {
        b
          .append("\n\n")
          .append(getChildTmpl
            .replace("{fieldpascal}", ct.getPascalName())
            .replace("{childtablepascal}", ct.getPascalName())
            .replace("{childtablecamel}", ct.getCamelName()));
      }
    }
    return b.toString();
  }

  private String generateSetters(Table t) {
    StringBuilder b = new StringBuilder();
    for (Column c : t.getColumns()) {
      if (c.isForeign()) {
        continue;
      }
      b
        .append("\n\n")
        .append(setterTmpl
          .replace("{datatype}", DataTypeUtil.resolvePrimitive(c))
          .replace("{columnpascal}", c.getPascalName())
          .replace("{columncamel}", c.getCamelName()));
    }

    for (Table pt : t.getParentTables()) {
      b
        .append("\n\n")
        .append(setterTmpl
          .replace("{datatype}", DataTypeUtil.resolvePrimitive(pt.getPrimaryColumn()))
          .replace("{columnpascal}", pt.getPrimaryColumn().getPascalName())
          .replace("{columncamel}", pt.getPrimaryColumn().getCamelName()))
        .append("\n\n")
        .append(setterTmpl
          .replace("{datatype}", pt.getPascalName())
          .replace("{columnpascal}", pt.getPascalName())
          .replace("{columncamel}", pt.getCamelName()));
    }

    for (Table ct : t.getChildTables()) {
      if (ct.isLooped() || ct.isJoined()) {
        Table primaryTable = ct.getPrimaryColumn().getForeignTable();
        Table secondaryTable = ct.getSecondaryColumn().getForeignTable();
        Table active = t.getPascalName().equals(primaryTable.getPascalName()) ? secondaryTable : primaryTable;
        b
          .append("\n\n")
          .append(setChildTmpl
            .replace("{fieldpascal}", ct.getPascalName())
            .replace("{childtablepascal}", active.getPascalName())
            .replace("{childtablecamel}", ct.getCamelName()));
      } else {
        b
          .append("\n\n")
          .append(setChildTmpl
            .replace("{fieldpascal}", ct.getPascalName())
            .replace("{childtablepascal}", ct.getPascalName())
            .replace("{childtablecamel}", ct.getCamelName()));
      }
    }
    return b.toString();
  }

  @Override
  protected void loadTemplates() {
    classTmpl = loadTemplate("class");
    childListTmpl = loadTemplate("childlist");
    parentObjTmpl = loadTemplate("parentobj");
    fieldTmpl = loadTemplate("field");
    getChildTmpl = loadTemplate("getchild");
    getterTmpl = loadTemplate("getter");
    setChildTmpl = loadTemplate("setchild");
    setterTmpl = loadTemplate("setter");
    joinedClassTmpl = loadTemplate("joinedclass");
  }
}

