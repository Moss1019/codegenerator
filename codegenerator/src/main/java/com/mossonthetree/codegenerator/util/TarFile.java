package com.mossonthetree.codegenerator.util;

import com.mossonthetree.codegeneratorlib.FileHandler;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;

public class TarFile implements AutoCloseable {
    private File internalFile;

    private final List<TarFileEntry> entries = new ArrayList<>();

    public byte[] getContent() {
        byte[] data = new byte[0];
        try (FileInputStream fIStream = new FileInputStream(internalFile)) {
            data = fIStream.readAllBytes();
        } catch (Exception ignored) {
        }
        return data;
    }

    public TarFile addEntry(String filePath, String fileContent) {
        entries.add(new TarFileEntry(filePath, fileContent));
        return this;
    }

    public TarFile flush() {
        String tempName = UUID.randomUUID() + ".gzip.tar";
        internalFile = new File(tempName);
        try (FileOutputStream fOStream = new FileOutputStream(internalFile);
             GZIPOutputStream gzipOStream = new GZIPOutputStream(fOStream);
             TarArchiveOutputStream tarOStream = new TarArchiveOutputStream(gzipOStream)) {

            for (TarFileEntry entry : entries) {
                String tempFileName = UUID.randomUUID().toString();
                FileHandler fHandler = new FileHandler(tempFileName);
                fHandler.writeFile(entry.content);
                File temp = new File(tempFileName);
                try (FileInputStream fIStream = new FileInputStream(temp);
                     BufferedInputStream bufIStream = new BufferedInputStream(fIStream)) {
                    tarOStream.putArchiveEntry(new TarArchiveEntry(temp, entry.path));
                    IOUtils.copy(bufIStream, tarOStream);
                    tarOStream.closeArchiveEntry();
                } catch (Exception ignored) {
                } finally {
                    temp.delete();
                }
            }
        } catch (Exception ignored) {
        }
        return this;
    }

    @Override
    public void close() {
        internalFile.delete();
    }

    private static class TarFileEntry {
        public String path;
        public String content;

        public TarFileEntry(String path, String content) {
            this.path = path;
            this.content = content;
        }
    }
}
