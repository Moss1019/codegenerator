package com.mossonthetree.codegenerator.generators;

import com.mossonthetree.codegenerator.CodeGenerateRequestBuilder;
import com.mossonthetree.codegenerator.ColumnBuilder;
import com.mossonthetree.codegenerator.DatabaseBuilder;
import com.mossonthetree.codegenerator.TableBuilder;
import com.mossonthetree.codegenerator.parameter.Environment;
import com.mossonthetree.codegenerator.view.CodeGenerateRequest;
import com.mossonthetree.codegeneratorlib.Database;
import com.mossonthetree.codegeneratorlib.DatabaseType;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

public class SpringBootGeneratorTests {
  @Test
  public void testSpringBootGenerator_callGenerate_returnsBytes() {
    // Arrange
    Database testDb = createTestDb();
    CodeGenerateRequest request = createTestCodeGenerateRequest();
    SpringBootGenerator sut = new SpringBootGenerator(testDb, request);

    // Act
    byte[] bytes = sut.generate();

    // Assert
    Assert.isTrue(bytes.length > 0, "No content generated");
  }

  private Database createTestDb() {
    return new DatabaseBuilder()
      .withRootName("todo")
      .withProjectName("example")
      .withType(DatabaseType.Sql)
      .addTable(new TableBuilder()
        .withName("agent")
        .addColumn(new ColumnBuilder()
          .withName("agent_id")
          .withDataType("int")
          .asPrimary()
          .asAutoIncrement()
          .build())
        .addColumn(new ColumnBuilder()
          .withName("username")
          .withDataType("string")
          .asUnique()
          .build())
        .build())
      .addTable(new TableBuilder()
        .withName("item")
        .addColumn(new ColumnBuilder()
          .withName("item_id")
          .withDataType("int")
          .asPrimary()
          .asAutoIncrement()
          .build())
        .addColumn(new ColumnBuilder()
          .withName("title")
          .withDataType("string")
          .build())
        .build())
      .build();
  }

  private CodeGenerateRequest createTestCodeGenerateRequest() {
    return new CodeGenerateRequestBuilder()
      .withEnvironment(Environment.java)
      .build();
  }
}
