package com.mossonthetree.codegeneratorlib.asp;

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
      return "Guid";
    }
    return "UNKNOWN " + d;
  }

  public static String resolveDefault(Column col) {
    String d = col.getDataType();
    if(d.equals(dInt)) {
      return "0";
    }
    if(d.equals(dString)) {
      return "string.Empty";
    }
    if(d.equals(dDate)) {
      return "new DateTime()";
    }
    if(d.equals(dBoolean)) {
      return "false";
    }
    if(d.equals(dGuid)) {
      return "Guid.Empty";
    }
    return "null!";
  }

  public static String resolveSqlType(Column col) {
    String d = col.getDataType();
    if(d.equals(dInt)) {
      return "integer";
    }
    if(d.equals(dString)) {
      return "char(16)";
    }
    if(d.equals(dDate)) {
      return "datetime2";
    }
    if(d.equals(dBoolean)) {
      return "bit";
    }
    if(d.equals(dGuid)) {
      return "uniqueidentifier";
    }
    return "integer";
  }

  public static String resovleSqlDefault(Column col) {
    String d = col.getDataType();
    if(d.equals(dInt)) {
      return "0";
    }
    if(d.equals(dString)) {
      return "''";
    }
    if(d.equals(dDate)) {
      return "getdate()";
    }
    if(d.equals(dBoolean)) {
      return "0";
    }
    if(d.equals(dGuid)) {
      return "newid()";
    }
    return "0";
  }
}
