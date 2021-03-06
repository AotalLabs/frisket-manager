package com.aotal.frisket.service;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by allan on 13/01/17.
 */
public interface DocumentService {


    /**
     * Checks that a Document exists in the storage service
     *
     * @param filename
     * @return the downloaded File
     */
    boolean documentPending(String filename);

    /**
     * Checks that a Document exists in the storage service
     *
     * @param filename
     * @return the downloaded File
     */
    boolean documentExists(String filename);

    /**
     * Uploads the file data to the storage service under the given filename
     *
     * @param filename
     * @param in
     * @throws IOException
     */
    void uploadDocument(String filename, InputStream in) throws IOException;

    /**
     * Removes the file from the storage service
     *
     * @param filename
     */
    void removeDocument(String filename);

    /**
     * Retrieves the specified document from the storage servicea
     *
     * @param filename
     * @return
     */
    InputStream getDocument(String filename);
}
