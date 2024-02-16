package com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.requestbody;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SmsRequestBody {
    private String deliverychannel;
    private Channels channels;
    private List<Destination> destination;


    public SmsRequestBody(String deliveryChannel, String text, List<String> msisdn, String correlationId)
    {
        this.deliverychannel = deliveryChannel;
        channels = new Channels(text);
        destination = new ArrayList<>();
        destination.add(new Destination(msisdn, correlationId));
    }



    @Data
    public class Channels{

        private Sms sms;
        public Channels(String text)
        {
            sms =  new Sms(text);
        }
    }

    @Data
    public class Sms{
        private String text;
        public Sms(String text)
        {
            this.text = text;
        }
    }

    @Data
    public class Destination{
        private List<String> msisdn;

//        @JsonProperty("correlation_id")
        private String correlationId;

        public Destination(List<String> msisdn, String correlationId)
        {
            this.msisdn = msisdn;
            this.correlationId = correlationId;
        }
    }
}
