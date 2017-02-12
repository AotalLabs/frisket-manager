package com.aotal.frisket.service;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by allan on 23/01/17.
 */
@Service
public class IOUtilFacade {

    public void copyAndClose(InputStream is, OutputStream os) throws IOException {
        IOUtils.copy(is, os);
        is.close();
    }

    public byte[] toByteArray(InputStream is) throws IOException {
        return IOUtils.toByteArray(is);
    }
}
