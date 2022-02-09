package com.mossonthetree.codegeneratorlib.elasticsearchasp;

import com.mossonthetree.codegeneratorlib.Column;
import com.mossonthetree.codegeneratorlib.DataTypeBase;

public class DataTypeUtil extends DataTypeBase {
  public static String resolvePrimitive(Column col) {
    String d = col.getDataType();
    if(d.equals(dInt)) {
      return "int";
    }
    if(d.equals(dString)) {
      return "string";
    }
    if(d.equals(dDate)) {
      return "DateTime";
    }
    if(d.equals(dBoolean)) {
      return "bool";
    }
    if(d.equals(dGuid)) {
      return "string";
    }
    return "UNKNOWN " + d;
  }
}
