package com.aotal.frisket.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by allan on 13/01/17.
 */
@Service
public class UnzipService implements DecompressService {
    private Logger LOG = LoggerFactory.getLogger(UnzipService.class);

    @Override
    public void decompress(InputStream stream, Path outdir) {

        byte[] buffer = new byte[1024];
        try {
            //get the zip file content
            ZipInputStream zis = new ZipInputStream(stream);
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String filename = Paths.get(ze.getName()).getFileName().toString();
                FileOutputStream fos = new FileOutputStream(new File(outdir.toString() + File.separator + filename));
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                ze = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
        } catch (IOException ex) {
            LOG.debug("Failed to unzip file", ex);
        }

    }
}
