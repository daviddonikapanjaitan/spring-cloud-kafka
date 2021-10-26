package guru.learningjournal.examples.kafka.kstreamaggregate.services;

import guru.learningjournal.examples.kafka.kstreamaggregate.bindings.EmployeeListenerBinding;
import guru.learningjournal.examples.kafka.model.Employee;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(EmployeeListenerBinding.class)
public class EmployeeStreamListener {

    @Autowired
    RecordBuilder recordBuilder;

    Logger log = LoggerFactory.getLogger(EmployeeStreamListener.class);

    @StreamListener("employee-input-channel")
    public void process(KStream<String, Employee> input) {

        input.peek((k, v) -> log.info("Key: {}, Value:{}", k, v))
                .groupBy((k, v) -> v.getDepartment())
                .aggregate(
                        () -> recordBuilder.init(),
                        (k, v, aggV) -> recordBuilder.aggregate(v, aggV)
                ).toStream()
                .foreach((k, v) -> log.info("Key = " + k + " Value = " + v.toString()));
    }
}
