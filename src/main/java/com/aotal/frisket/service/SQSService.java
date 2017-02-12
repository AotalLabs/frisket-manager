package com.aotal.frisket.service;

import com.amazonaws.services.sqs.AmazonSQS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * Created by allan on 13/01/17.
 */
@Service
public class SQSService implements QueueService {

    private final String queueName;
    private final AmazonSQS amazonSqs;

    @Inject
    public SQSService(@Value("#{environment.AWS_SERVICE_PREFIX}") String queueName, AmazonSQS amazonSqs) {
        this.queueName = queueName;
        this.amazonSqs = amazonSqs;
    }

    @Override
    public void putMessage(String message) {
        amazonSqs.sendMessage(amazonSqs.getQueueUrl(queueName).getQueueUrl(), message);
    }
}
