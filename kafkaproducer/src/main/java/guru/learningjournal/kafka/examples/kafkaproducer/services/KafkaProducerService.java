package guru.learningjournal.kafka.examples.kafkaproducer.services;

import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class KafkaProducerService {

    Logger LOG = LoggerFactory.getLogger(KafkaProducerService.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, String key, String value){

        LOG.info(String.format("Producing Message - Key: %s, Value %s to topic %s", key, value, topic));
        kafkaTemplate.send(topic, key, value);
    }
}