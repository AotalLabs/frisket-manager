package com.aotal.frisket.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by allan on 13/01/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class S3ServiceTest {
    private static final String PENDING_BUCKET = "null-pending";
    private static final String DONE_BUCKET = "null-done";

    @Mock
    private S3Object mock;

    @Mock
    private AmazonS3 s3;

    @InjectMocks
    private S3Service service;

    @Test
    public void testDocumentPendingChecksPendingBucket() {
        service.documentPending(null);
        verify(s3, times(1)).doesObjectExist(PENDING_BUCKET, null);
    }

    @Test
    public void testDocumentExistsChecksDoneBucket() {
        service.documentExists(null);
        verify(s3, times(1)).doesObjectExist(DONE_BUCKET, null);
    }

    @Test
    public void testUploadDocumentUsesPendingBucket() throws IOException {
        service.uploadDocument(null, null);
        verify(s3, times(1)).putObject(PENDING_BUCKET, null, null, null);
    }

    @Test
    public void testRemoveDocumentUsesPendingBucket() {
        service.removeDocument(null);
        verify(s3, times(1)).deleteObject(PENDING_BUCKET, null);
    }

    @Test
    public void testGetDocumentReturnsStreamFromDoneBucket() {
        given(s3.getObject(DONE_BUCKET, null)).willReturn(mock);
        service.getDocument(null);
        verify(s3, times(1)).getObject(DONE_BUCKET, null);
        verify(mock, times(1)).getObjectContent();
    }
}
