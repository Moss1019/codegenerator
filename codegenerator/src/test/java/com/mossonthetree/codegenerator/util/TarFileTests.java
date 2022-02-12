package com.mossonthetree.codegenerator.util;

import com.mossonthetree.codegeneratorlib.FileHandler;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class TarFileTests {
    @Test
    public void testTarFile_addFileToArchive() {
        // Arrange
        TarFile sut = new TarFile();

        // Act
        sut
            .addEntry("content/settings.conf", "hello world of tars")
            .addEntry("content/configuration.conf", "Hello world")
            .flush();

        // Assert
        byte[] data = sut.getContent();
        Assert.isTrue(data.length > 0, "No file data");

        File temp = new File("temp.gzip.tar");
        try(FileOutputStream fOStream = new FileOutputStream(temp);) {
            fOStream.write(data, 0, data.length);
        } catch (Exception ignored){}

        try(FileInputStream fIStream = new FileInputStream(temp);
            GZIPInputStream gzipIStream = new GZIPInputStream(fIStream);
            TarArchiveInputStream tarIStream = new TarArchiveInputStream(gzipIStream)) {

            TarArchiveEntry entry = tarIStream.getNextTarEntry();
            Assert.isTrue(entry.getName().equals("content/settings.conf"), "File path not equal: "  + entry.getPath());
        } catch (Exception ex) {
            Assert.isTrue(false, ex.getMessage());
        }

        temp.delete();
        sut.close();
    }
}
