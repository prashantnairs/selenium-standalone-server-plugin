package com.lazerycode.selenium.download;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ExtractFilesFromZip {

    public ExtractFilesFromZip(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    private String localFilePath;
    private String extractedFileAbsolutePath = "";

    /**
     * Unzip a downloaded zip file (this will implicitly overwrite any existing files)
     *
     * @param zipToDownload
     * @throws java.io.IOException
     */
    public void unzipFile(File zipToDownload) throws IOException {
        ZipFile zip = new ZipFile(zipToDownload);
        Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zipFileEntry = entries.nextElement();
            File extractedFile = new File(this.localFilePath, zipFileEntry.getName());
            if (zipFileEntry.isDirectory()) continue;
            extractedFile.getParentFile().mkdirs();
            extractedFile.createNewFile();
            InputStream is = zip.getInputStream(zipFileEntry);
            OutputStream os = new FileOutputStream(extractedFile);
            while (is.available() > 0) {
                os.write(is.read());
            }
            os.close();
            is.close();
            //TODO this is a bit flaky, what if last file extracted is not what we expect?
            this.extractedFileAbsolutePath = extractedFile.getAbsolutePath();
        }
        zip.close();
    }

    /**
     * Return the absolute path of the last file extracted from the Zip.
     *
     * @return
     */
    public String getExtractedFileAbsolutePath() {
        return this.extractedFileAbsolutePath;
    }
}