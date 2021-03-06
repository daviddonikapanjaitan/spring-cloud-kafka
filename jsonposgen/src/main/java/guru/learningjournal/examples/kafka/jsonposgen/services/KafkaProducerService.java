package guru.learningjournal.examples.kafka.jsonposgen.services;

import guru.learningjournal.examples.kafka.jsonposgen.model.PosInvoice;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class KafkaProducerService {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaProducerService.class);

    @Value("${application.configs.topic.name}")
    private String TOPIC_NAME;

    @Autowired
    private KafkaTemplate<String, PosInvoice> kafkaTemplate;

    public void sendMessage(PosInvoice invoice){
        LOG.info(String.format("Producing Invoice No: %s", invoice.getInvoiceNumber()));
        kafkaTemplate.send(TOPIC_NAME, invoice.getStoreID(), invoice);
    }
}