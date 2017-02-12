package com.aotal.frisket.service;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * Created by allan on 13/01/17.
 */
public interface DecompressService {

    /**
     * Decompresses the input stream into the given output directory
     *
     * @param stream
     * @param outdir
     */
    void decompress(InputStream stream, Path outdir);
}
