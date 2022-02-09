package com.mossonthetree.codegeneratorlib.python;

import com.mossonthetree.codegeneratorlib.Column;
import com.mossonthetree.codegeneratorlib.DataTypeBase;

public class DataTypeUtil extends DataTypeBase {
  public static String resolveModelType(Column col) {
    if(col.isAutoIncrement()) {
      return "AutoField";
    }
    if(col.isForeign()) {
      return "ForeignKey";
    }
    String d = col.getDataType();
    if(d.equals(dInt)) {
      return "IntegerField";
    }
    if(d.equals(dString)) {
      return "CharField";
    }
    if(d.equals(dDate)) {
      return "DateTimeField";
    }
    if(d.equals(dBoolean)) {
      return "BooleanField";
    }
    if(d.equals(dGuid)) {
      return "UUIDField";
    }
    return "IntegerField";
  }

  public static String resolveBasicOption(Column col) {
    String d = col.getDataType();
    if(d.equals(dString)) {
      return "max_length=32";
    }
    return "";
  }

  public static String resolvePrimaryAssignment(Column col) {
    String d = col.getDataType();
    if(d.equals(dGuid)) {
      return "new_{tablename}.set_field('{primaryname}', UUID())\n    ";
    }
    return "";
  }

  public static String getRegexType(Column col) {
    String d = col.getDataType();
    if(d.equals(dString)) {
      return "\\w+";
    }
    if(d.equals(dGuid)) {
      return "[0-9a-zA-Z-]+";
    }
    return "\\d+";
  }
}
