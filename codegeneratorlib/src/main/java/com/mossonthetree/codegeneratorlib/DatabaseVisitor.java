package com.mossonthetree.codegeneratorlib;

import com.mossonthetree.codegeneratorlib.parser.*;

import java.util.*;

public class DatabaseVisitor extends DatabaseBaseVisitor<Object> {
  private boolean valid;
  private String errorMsg;

  public boolean isValid() {
    return valid;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  @Override
  public List<Table> visitDatabase(DatabaseParser.DatabaseContext ctx) {
    List<Table> tables = new ArrayList<>();
    for(DatabaseParser.TableContext tCtx: ctx.table()) {
      tables.add(visitTable(tCtx));
    }
    return tables;
  }

  public Table visitTable(DatabaseParser.TableContext ctx) {
    String name = ctx.NAME().getText();
    boolean joined = ctx.joined() != null;
    boolean looped = ctx.looped() != null;
    String loopedJoinedName = joined ? ctx.joined().NAME().getText() : looped ? ctx.looped().NAME().getText() : "";
    List<Column> columns = new ArrayList<>();
    for(DatabaseParser.ColumnContext cCtx: ctx.column()) {
      columns.add(visitColumn(cCtx));
    }
    return new Table(name, joined, looped, loopedJoinedName, columns);
  }

  public Column visitColumn(DatabaseParser.ColumnContext ctx) {
    String name = ctx.NAME().getText();
    String dataType = ctx.DATA_TYPE().getText();
    boolean primary = false;
    boolean secondary = false;
    boolean autoIncrement = false;
    boolean unique = false;
    boolean foreign = false;
    String foreignTableName = null;
    for(DatabaseParser.OptionContext oCtx: ctx.option()) {
      if(oCtx.getText().equals("primary")) {
        primary = true;
      }
      if(oCtx.getText().equals("secondary")) {
        secondary = true;
      }
      if(oCtx.getText().equals("auto_increment")) {
        autoIncrement = true;
      }
      if(oCtx.getText().equals("unique")) {
        unique = true;
      }
      if(oCtx.getText().contains("foreign")) {
        foreign = true;
        foreignTableName = oCtx.NAME().getText();
      }
    }
    return new Column(name, dataType, foreignTableName, primary, secondary, autoIncrement, foreign, unique);
  }
}
