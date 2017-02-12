package com.aotal.frisket.controller;

import com.aotal.frisket.service.CompressService;
import com.aotal.frisket.service.DecompressService;
import com.aotal.frisket.service.DocumentService;
import com.aotal.frisket.service.Encoder;
import com.aotal.frisket.service.IOUtilFacade;
import com.aotal.frisket.service.QueueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

/**
 * Created by allan on 13/01/17.
 */
@RestController
public class Frisket {

    private final DocumentService documentService;
    private final DecompressService decompressService;
    private final CompressService compressService;
    private final Encoder encoder;
    private final QueueService queueService;
    private final IOUtilFacade ioUtilFacade;

    @Inject
    public Frisket(DocumentService documentService, DecompressService decompressService, CompressService compressService, Encoder encoder, QueueService queueService, IOUtilFacade ioUtilFacade) {
        this.documentService = documentService;
        this.decompressService = decompressService;
        this.compressService = compressService;
        this.encoder = encoder;
        this.queueService = queueService;
        this.ioUtilFacade = ioUtilFacade;
    }

    @RequestMapping(value = "/print", method = RequestMethod.POST)
    public ResponseEntity<String> print(HttpServletRequest request) throws IOException, NoSuchAlgorithmException {
        byte[] tarContents = ioUtilFacade.toByteArray(request.getInputStream());
        String encoding = encoder.encode(tarContents);
        if (documentService.documentPending(encoding)) {
            return new ResponseEntity<>(encoding, HttpStatus.OK);
        }
        documentService.uploadDocument(encoding, new ByteArrayInputStream(tarContents));
        queueService.putMessage(encoding);
        return new ResponseEntity<>(encoding, HttpStatus.OK);
    }

    @RequestMapping(value = "/printzip", method = RequestMethod.POST)
    public ResponseEntity<String> printzip(HttpServletRequest request) throws IOException, NoSuchAlgorithmException {
        byte[] zippedContents = ioUtilFacade.toByteArray(request.getInputStream());
        String encoding = encoder.encode(zippedContents);
        if (documentService.documentPending(encoding)) {
            return new ResponseEntity<>(encoding, HttpStatus.OK);
        }
        Path outdir = Files.createTempDirectory("temp");
        outdir.toFile().deleteOnExit();
        decompressService.decompress(new ByteArrayInputStream(zippedContents), outdir);
        Path archive = compressService.compress(outdir);
        archive.toFile().deleteOnExit();
        documentService.uploadDocument(encoding, Files.newInputStream(archive));
        queueService.putMessage(encoding);
        return new ResponseEntity<>(encoding, HttpStatus.OK);
    }

    @RequestMapping(value = "/pdf/{file}", method = RequestMethod.GET)
    public void getResult(@PathVariable("file") String filename, HttpServletResponse response) throws IOException, NoSuchAlgorithmException {
        if (documentService.documentExists(filename + ".pdf")) {
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=\"" + filename + ".pdf\"");
            ioUtilFacade.copyAndClose(documentService.getDocument(filename + ".pdf"), response.getOutputStream());
            response.flushBuffer();
        } else if (documentService.documentPending(filename)) {
            response.sendError(HttpStatus.NO_CONTENT.value());
        } else {
            response.sendError(HttpStatus.NOT_FOUND.value());
        }
    }
}
