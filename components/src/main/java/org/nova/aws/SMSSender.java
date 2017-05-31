package org.nova.aws;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

public class SMSSender
{
    final private AmazonSNSClient client;

    public SMSSender(String accessKey, String secretKey)
    {
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        this.client = new AmazonSNSClient(awsCredentials);
        client.setRegion(Region.getRegion(Regions.DEFAULT_REGION));
    }

    public void sendSMSMessage(String phoneNumber, String message)
    {
        Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();

        PublishResult result = this.client.publish(new PublishRequest().withMessage(message).withPhoneNumber(phoneNumber).withMessageAttributes(smsAttributes));
    }
}
