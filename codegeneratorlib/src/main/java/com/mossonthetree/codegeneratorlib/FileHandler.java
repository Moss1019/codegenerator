package com.mossonthetree.codegeneratorlib;

import java.io.*;
import java.net.URL;

public class FileHandler {
    private final String fileName;

    private boolean inError;

    public FileHandler(String fileName) {
        this.fileName = fileName;
        inError = false;
    }

    public FileHandler(URL fileUrl) {
        fileName = fileUrl.getPath();
        inError = false;
    }

    public boolean isInError() {
        return inError;
    }

    public void writeFile(String content) {
        File f = new File(fileName);
        FileOutputStream fOut = null;
        BufferedOutputStream bufOStream = null;
        try {
            fOut = new FileOutputStream(f);
            bufOStream = new BufferedOutputStream(fOut);
            bufOStream.write(content.getBytes());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            inError = true;
        } finally {
            try {
                bufOStream.close();
            } catch (Exception ex) {
            }
            try {
                fOut.close();
            } catch (Exception ex) {
            }
        }
    }

    public String readFile() {
        String fileContent;
        FileInputStream fiStream = null;
        BufferedInputStream bufStream = null;
        try {
            fiStream = new FileInputStream(fileName);
            bufStream = new BufferedInputStream(fiStream);
            fileContent = new String(bufStream.readAllBytes());
        } catch (IOException ex) {
            fileContent = "";
            inError = true;
        } finally {
            try {
                bufStream.close();
            } catch (Exception ex) {
            }
            try {
                fiStream.close();
            } catch (Exception ex) {
            }
        }
        return fileContent;
    }
}
