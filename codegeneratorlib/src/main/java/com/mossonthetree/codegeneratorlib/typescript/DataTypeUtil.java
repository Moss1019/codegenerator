package com.mossonthetree.codegeneratorlib.typescript;

import com.mossonthetree.codegeneratorlib.Column;
import com.mossonthetree.codegeneratorlib.DataTypeBase;

public class DataTypeUtil extends DataTypeBase {
  public static String resolveType(Column col) {
    String d = col.getDataType();
    if(d.equals(dInt)) {
      return "number";
    }
    if(d.equals(dDate)) {
      return "Date";
    }
    if(d.equals(dBoolean)) {
      return "boolean";
    }
    return "string";
  }
}
