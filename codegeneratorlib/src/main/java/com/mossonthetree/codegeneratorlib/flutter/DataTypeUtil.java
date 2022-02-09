package com.mossonthetree.codegeneratorlib.flutter;

import com.mossonthetree.codegeneratorlib.Column;
import com.mossonthetree.codegeneratorlib.DataTypeBase;

public class DataTypeUtil extends DataTypeBase {
  public static String resolvePrimitive(Column col) {
    String d = col.getDataType();
    if(d.equals(dInt)) {
      return "int";
    }
    if(d.equals(dString)) {
      return "String";
    }
    if(d.equals(dDate)) {
      return "DateTime";
    }
    if(d.equals(dGuid)) {
      return "String";
    }
    if(d.equals(dBoolean)) {
      return "bool";
    }
    return "UNKNOWN " + d;
  }

  public static String resolveDefault(Column col) {
    String d = col.getDataType();
    if(d.equals(dInt)) {
      return "0";
    }
    if(d.equals(dString)) {
      return "''";
    }
    if(d.equals(dDate)) {
      return "DateTime.now()";
    }
    if(d.equals(dBoolean)) {
      return "false";
    }
    if(d.equals(dGuid)) {
      return "''";
    }
    return "null";
  }
}
