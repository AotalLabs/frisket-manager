package com.aotal.frisket.service;

import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by allan on 13/01/17.
 */
@Service
public class TarService implements CompressService {

    @Override
    public Path compress(Path directory) throws IOException {
        Path tempFile = Files.createTempFile("temp", "tar.gz");
        OutputStream tarOutput = new FileOutputStream(tempFile.toFile());
        ArchiveOutputStream tarArchive = new TarArchiveOutputStream(new GzipCompressorOutputStream(tarOutput));

        File[] files = directory.toFile().listFiles();
        if (files == null) {
            files = new File[0];
        }
        for (File file : files) {
            TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(file, file.getName());
            tarArchiveEntry.setSize(file.length());
            tarArchive.putArchiveEntry(tarArchiveEntry);
            FileInputStream fileInputStream = new FileInputStream(file);
            IOUtils.copy(fileInputStream, tarArchive);
            fileInputStream.close();
            tarArchive.closeArchiveEntry();
        }

        tarArchive.finish();
        tarArchive.close();
        tarOutput.close();
        return tempFile;
    }
}
