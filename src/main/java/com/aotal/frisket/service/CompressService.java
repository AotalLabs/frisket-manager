package com.aotal.frisket.service;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by allan on 13/01/17.
 */
public interface CompressService {

    /**
     * Compresses the given directory
     *
     * @param directory
     * @return the compressed file
     * @throws IOException
     */
    Path compress(Path directory) throws IOException;
}
