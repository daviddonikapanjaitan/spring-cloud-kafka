package guru.learningjournal.examples.kafka.jsonposfanout.services;

import guru.learningjournal.examples.kafka.jsonposfanout.bindings.PosListenerBinding;
import guru.learningjournal.examples.kafka.jsonposfanout.model.PosInvoice;
import guru.learningjournal.examples.kafka.model.Notification;
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
public class NotificationProcessorService {

    Logger LOG = LoggerFactory.getLogger(NotificationProcessorService.class);

    @Autowired
    RecordBuilder recordBuilder;

    @StreamListener("notification-input-channel")
    @SendTo("notification-output-channel")
    public KStream<String, Notification> process(KStream<String, PosInvoice> input) {

        KStream<String, Notification> notificationKStream = input
                .filter((k, v) -> v.getCustomerType().equalsIgnoreCase("PRIME"))
                .mapValues(v -> recordBuilder.getNotification(v));

        notificationKStream.foreach((k, v) -> LOG.info(String.format("Notification:- Key: %s, Value: %s", k, v)));
        return notificationKStream;
    }
}