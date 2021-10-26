package guru.learningjournal.examples.kafka.exactlyoncefanout.services;

import guru.learningjournal.examples.kafka.exactlyoncefanout.bindings.PosListenerBinding;
import guru.learningjournal.examples.kafka.model.HadoopRecord;
import guru.learningjournal.examples.kafka.model.Notification;
import guru.learningjournal.examples.kafka.model.PosInvoice;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(PosListenerBinding.class)
public class PosListenerService {

    Logger log = LoggerFactory.getLogger(PosListenerService.class);

    @Autowired
    RecordBuilder recordBuilder;

    @StreamListener("pos-input-channel")
    public void process(KStream<String, PosInvoice> input) {

        KStream<String, HadoopRecord> hadoopRecordKStream = input
                .mapValues( v -> recordBuilder.getMaskedInvoice(v))
                .flatMapValues( v -> recordBuilder.getHadoopRecords(v));

        KStream<String, Notification> notificationKStream = input
                .filter((k, v) -> v.getCustomerType().equalsIgnoreCase("PRIME"))
                .mapValues(v -> recordBuilder.getNotification(v));

        hadoopRecordKStream.foreach((k, v) -> log.info(String.format("Hadoop Record:- Key: %s, Value: %s", k, v)));
        notificationKStream.foreach((k, v) -> log.info(String.format("Notification:- Key: %s, Value: %s", k, v)));

        hadoopRecordKStream.to("hadoop-sink-topic");
        notificationKStream.to("loyalty-topic");
    }
}