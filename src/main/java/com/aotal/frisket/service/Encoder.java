package com.aotal.frisket.service;

import java.security.NoSuchAlgorithmException;

/**
 * Created by allan on 13/01/17.
 */
public interface Encoder {

    /**
     * Encodes the given bytes using a hash algorithm
     *
     * @param bytes to hash
     * @return the String representation of the hashed bytes
     * @throws NoSuchAlgorithmException if the container does not contain the hashing algorithm
     */
    String encode(byte[] bytes) throws NoSuchAlgorithmException;
}
