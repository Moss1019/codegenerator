package com.mossonthetree.codegenerator.util;

import com.mossonthetree.codegenerator.view.CodeGenerateRequest;
import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.DatabaseVisitor;
import com.mossonthetree.codegeneratorlib.Table;
import com.mossonthetree.codegeneratorlib.parser.DatabaseLexer;
import com.mossonthetree.codegeneratorlib.parser.DatabaseParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;

public class DatabaseFactory {
  public Database createDatabase(CodeGenerateRequest request) {
    try {
      CharStream antlrIStream = CharStreams.fromString(request.getDefinition());
      DatabaseLexer lexer = new DatabaseLexer(antlrIStream);
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      DatabaseParser parser = new DatabaseParser(tokens);
      ParseTree tree = parser.database();
      DatabaseVisitor visitor = new DatabaseVisitor();

      Database db = new Database(request.getRootName(),
        request.getProjectName(),
        request.getDatabaseType(),
        (List<Table>) visitor.visit(tree));

      return db;
    } catch (Exception ex) {
      System.out.println("Error while creating Database: " + ex.toString());
      ex.printStackTrace();
    }
    return null;
  }
}
