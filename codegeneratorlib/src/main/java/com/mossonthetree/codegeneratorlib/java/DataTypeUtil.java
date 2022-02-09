package com.mossonthetree.codegeneratorlib.java;

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
      return "LocalDateTime";
    }
    if(d.equals(dBoolean)) {
      return "bool";
    }
    if(d.equals(dGuid)) {
      return "UUID";
    }
    return "UNKNOWN " + d;
  }

  public static String resolveWrapper(Column col) {
    String d = col.getDataType();
    if(d.equals(dInt)) {
      return "Integer";
    }
    if(d.equals(dString)) {
      return "String";
    }
    if(d.equals(dDate)) {
      return "LocalDateTime";
    }
    if(d.equals(dBoolean)) {
      return "Boolean";
    }
    if(d.equals(dGuid)) {
      return "UUID";
    }
    return "UNKNOWN " + d;
  }

  public static String resolveDefault(Column col) {
    String d = col.getDataType();
    if(d.equals(dInt)) {
      return "0";
    }
    if(d.equals(dString)) {
      return "\"\"";
    }
    if(d.equals(dBoolean)) {
      return "false";
    }
    if(d.equals(dGuid)) {
      return "new UUID(0L, 0L)";
    }
    return "null";
  }
}
