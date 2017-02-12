package com.aotal.frisket.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

/**
 * Created by allan on 13/01/17.
 */
@Service
public class SHA256Encoder implements Encoder {

    @Override
    public String encode(byte[] bytes) throws NoSuchAlgorithmException {
        return DigestUtils.sha256Hex(bytes);
    }
}
