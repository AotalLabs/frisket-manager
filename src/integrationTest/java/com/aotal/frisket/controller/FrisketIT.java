package com.aotal.frisket.controller;

import com.aotal.frisket.service.CompressService;
import com.aotal.frisket.service.DecompressService;
import com.aotal.frisket.service.DocumentService;
import com.aotal.frisket.service.Encoder;
import com.aotal.frisket.service.IOUtilFacade;
import com.aotal.frisket.service.QueueService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.inject.Inject;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by allan on 23/01/17.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(Frisket.class)
public class FrisketIT {

    @Inject
    private MockMvc mvc;

    @MockBean
    private DocumentService documentService;
    @MockBean
    private DecompressService decompressService;
    @MockBean
    private CompressService compressService;
    @MockBean
    private Encoder encoder;
    @MockBean
    private QueueService queueService;
    @MockBean
    private IOUtilFacade ioUtilFacade;

    @Test
    public void testPrintEndpoint() throws Exception {
        given(ioUtilFacade.toByteArray(any(InputStream.class))).willReturn(new byte[0]);
        mvc.perform(post("/print"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    public void testPrintzipEndpoint() throws Exception {
        given(ioUtilFacade.toByteArray(any(InputStream.class))).willReturn(new byte[0]);
        given(compressService.compress(any(Path.class))).willReturn(Files.createTempDirectory("tempcompress"));
        mvc.perform(post("/printzip"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    public void testGetEndpoint() throws Exception {
        given(documentService.documentExists("test.pdf")).willReturn(true);
        mvc.perform(get("/pdf/test"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
