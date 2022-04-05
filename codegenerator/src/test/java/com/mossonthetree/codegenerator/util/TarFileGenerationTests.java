package com.mossonthetree.codegenerator.util;

import com.mossonthetree.codegeneratorlib.FileHandler;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.io.*;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class TarFileGenerationTests {
    @Test
    public void createTarEntries_ReadEntries_ValidContent() {
        // Arrange
        String path = "content/settings.conf";
        String content = "Hello World!";
        String tarFileName = "temp.gzip.tar";
        String tempFileName = UUID.randomUUID().toString();

        // Act
        File tarFile = new File(tarFileName);
        try (FileOutputStream fOStream = new FileOutputStream(tarFile);
             GZIPOutputStream gzipOStream = new GZIPOutputStream(fOStream);
             TarArchiveOutputStream tarOStream = new TarArchiveOutputStream(gzipOStream)) {

            FileHandler fHandler = new FileHandler(tempFileName);
            fHandler.writeFile(content);
            File temp = new File(tempFileName);
            try (FileInputStream fIStream = new FileInputStream(temp);
                 BufferedInputStream bufIStream = new BufferedInputStream(fIStream)) {
                tarOStream.putArchiveEntry(new TarArchiveEntry(temp, path));
                IOUtils.copy(bufIStream, tarOStream);
                tarOStream.closeArchiveEntry();
            } catch (Exception ignored) {
            } finally {
                temp.delete();
            }
        } catch (Exception ignored) {
        }

        // Assert
        try (FileInputStream fIStream = new FileInputStream(tarFile);
             GZIPInputStream gzipIStream = new GZIPInputStream(fIStream);
             TarArchiveInputStream tarIStream = new TarArchiveInputStream(gzipIStream)) {

            TarArchiveEntry entry = tarIStream.getNextTarEntry();
            File entryFile = entry.getFile();
            Assert.isTrue(entryFile.equals(path), "File path not equal: " + entryFile.getPath());
            try (FileInputStream tarFileIStream = new FileInputStream(entryFile)) {
                String fileContent = new String(tarFileIStream.readAllBytes());
                Assert.isTrue(fileContent.equals(content), "File content not equal: " + fileContent);
            } catch (Exception ignored) {
            }
        } catch (Exception ignored) {
        }
        tarFile.delete();
    }
}
