package com.mossonthetree.codegenerator.util;

import com.mossonthetree.codegeneratorlib.FileHandler;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;

public class TarFile implements AutoCloseable {
  private File internalFile;
  private FileOutputStream fOut;
  private GZIPOutputStream gzipOut;
  private TarArchiveOutputStream tarOutput;

  public TarFile(String tarFileName) {
    internalFile = new File(tarFileName);
    try {
      fOut = new FileOutputStream(internalFile);
      gzipOut = new GZIPOutputStream(fOut);
      tarOutput = new TarArchiveOutputStream(gzipOut);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public byte[] getContent() {
    byte[] data = new byte[0];
    try(FileInputStream fIn = new FileInputStream(internalFile)) {
      data = fIn.readAllBytes();
    } catch (Exception ignored) {}
    return data;
  }

  public boolean addEntry(String filePath, String fileContent) {
    String tempFileName = UUID.randomUUID().toString();
    FileHandler writer = new FileHandler(tempFileName);
    writer.writeFile(fileContent);
    File temp = new File(tempFileName);
    try (FileInputStream fIn = new FileInputStream(temp);
         BufferedInputStream bIn = new BufferedInputStream(fIn)) {
      tarOutput.putArchiveEntry(new TarArchiveEntry(temp, filePath));
      IOUtils.copy(bIn, tarOutput);
      tarOutput.closeArchiveEntry();
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    } finally {
      temp.delete();
    }
    return true;
  }

  @Override
  public void close() {
    internalFile.delete();
    try {
      tarOutput.close();
    } catch (Exception ignored) {}
    try {
      gzipOut.close();
    } catch (Exception ignored) {}
    try {
      fOut.close();
    } catch (Exception ignored) {}
  }
}
