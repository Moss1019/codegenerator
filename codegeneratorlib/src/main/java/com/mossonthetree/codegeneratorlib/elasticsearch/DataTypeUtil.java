package com.mossonthetree.codegeneratorlib.elasticsearch;

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
    if(d.equals(dBoolean)) {
      return "boolean";
    }
    if(d.equals(dGuid)) {
      return "String";
    }
    return "UNKNOWN " + d;
  }
}
