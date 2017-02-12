package com.aotal.frisket.controller;

import com.aotal.frisket.service.CompressService;
import com.aotal.frisket.service.DecompressService;
import com.aotal.frisket.service.DocumentService;
import com.aotal.frisket.service.Encoder;
import com.aotal.frisket.service.IOUtilFacade;
import com.aotal.frisket.service.QueueService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Created by allan on 23/01/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class FrisketTest {

    @Mock
    private DocumentService documentService;
    @Mock
    private DecompressService decompressService;
    @Mock
    private CompressService compressService;
    @Mock
    private Encoder encoder;
    @Mock
    private QueueService queueService;
    @Mock
    private IOUtilFacade ioUtilFacade;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private Frisket frisket;

    @Test
    public void testPrintDoesntDuplicateJobs() throws IOException, NoSuchAlgorithmException {
        byte[] testData = new byte[0];
        given(ioUtilFacade.toByteArray(null)).willReturn(testData);
        given(documentService.documentPending(null)).willReturn(true);
        ResponseEntity result = frisket.print(request);
        verify(encoder, times(1)).encode(testData);
        assertThat("Return code was incorrect", result.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void testPrintzipDoesntDuplicateJobs() throws IOException, NoSuchAlgorithmException {
        byte[] testData = new byte[0];
        given(ioUtilFacade.toByteArray(null)).willReturn(testData);
        given(documentService.documentPending(null)).willReturn(true);
        ResponseEntity result = frisket.printzip(request);
        verify(encoder, times(1)).encode(testData);
        assertThat("Return code was incorrect", result.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void testGetDocument() throws IOException, NoSuchAlgorithmException {
        given(documentService.documentExists("null.pdf")).willReturn(true);
        frisket.getResult(null, response);
        verify(ioUtilFacade, times(1)).copyAndClose(null, null);
        verify(response, times(1)).setContentType("application/pdf");
        verify(response, times(1)).addHeader("Content-Disposition", "attachment; filename=\"null.pdf\"");
        verify(documentService, times(1)).getDocument("null.pdf");
    }

    @Test
    public void testGetDocumentNotFoundReturns404() throws IOException, NoSuchAlgorithmException {
        given(documentService.documentExists("null.pdf")).willReturn(false);
        given(documentService.documentPending(null)).willReturn(false);
        frisket.getResult(null, response);
        verify(response, times(1)).sendError(404);
    }

    @Test
    public void testGetDocumentNotDoneReturns204() throws IOException, NoSuchAlgorithmException {
        given(documentService.documentExists("null.pdf")).willReturn(false);
        given(documentService.documentPending(null)).willReturn(true);
        frisket.getResult(null, response);
        verify(response, times(1)).sendError(204);
    }
}
