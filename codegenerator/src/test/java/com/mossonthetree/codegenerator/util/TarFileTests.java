package com.mossonthetree.codegenerator.util;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

public class TarFileTests {
  @Test
  public void testTarFile_addFileToArchive() {
    // Arrange
    TarFile sut = new TarFile("file");

    // Act
    sut.addEntry("content/file.txt", "hello world");

    // Assert
    Assert.isTrue(sut.getContent().length > 0, "No file data");

    sut.close();
  }
}
