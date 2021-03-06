package guru.learningjournal.examples.kafka.streamingaggregates.services;

import guru.learningjournal.examples.kafka.streamingaggregates.bindings.WordListenerBinding;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@EnableBinding(WordListenerBinding.class)
public class WordListenerService {

    Logger log = LoggerFactory.getLogger(WordListenerService.class);

    @StreamListener("words-input-channel")
    public void process(KStream<String, String> input) {

        KStream<String, String> wordStream = input
                .flatMapValues(value -> Arrays.asList(value.toLowerCase().split(" ")));

        wordStream.groupBy((key, value) -> value)
                .count()
                .toStream()
                .peek((k, v) -> log.info("Word: {} Count: {}", k, v));
    }
}