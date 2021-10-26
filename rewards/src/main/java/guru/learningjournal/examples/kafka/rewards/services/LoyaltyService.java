package guru.learningjournal.examples.kafka.rewards.services;

import guru.learningjournal.examples.kafka.model.Notification;
import guru.learningjournal.examples.kafka.model.PosInvoice;
import guru.learningjournal.examples.kafka.rewards.bindings.PosListenerBinding;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(PosListenerBinding.class)
public class LoyaltyService {

    @Autowired
    RecordBuilder recordBuilder;

    Logger log = LoggerFactory.getLogger(LoyaltyService.class);

    @StreamListener("invoice-input-channel")
    @SendTo("notification-output-channel")
    public KStream<String, Notification> process(KStream<String, PosInvoice> input) {

        KStream<String, Notification> notificationKStream = input
                .filter((k, v) -> v.getCustomerType().equalsIgnoreCase("PRIME"))
                .map((k, v) -> new KeyValue<>(v.getCustomerCardNo(), recordBuilder.getNotification(v)))
                .groupByKey()
                .reduce((aggValue, newValue) -> {
                    newValue.setTotalLoyaltyPoints(newValue.getEarnedLoyaltyPoints() + aggValue.getTotalLoyaltyPoints());
                    return newValue;
                }).toStream();

        notificationKStream.foreach((k, v) -> log.info(String.format("Notification:- Key: %s, Value: %s", k, v)));
        return notificationKStream;
    }
}
