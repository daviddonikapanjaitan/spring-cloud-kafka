package guru.learningjournal.examples.kafka.jsonposfanout.bindings;

import guru.learningjournal.examples.kafka.jsonposfanout.model.PosInvoice;
import guru.learningjournal.examples.kafka.model.HadoopRecord;
import guru.learningjournal.examples.kafka.model.Notification;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.context.annotation.Bean;

@EnableBinding
public interface PosListenerBinding {

    @Bean
    @Input("notification-input-channel")
    KStream<String, PosInvoice> notificationInputStream();

    @Bean
    @Output("notification-output-channel")
    KStream<String, Notification> notificationOutputStream();

    @Bean
    @Input("hadoop-input-channel")
    KStream<String, PosInvoice> hadoopInputStream();

    @Bean
    @Output("hadoop-output-channel")
    KStream<String, HadoopRecord> hadoopOutputStream();
}