package guru.learningjournal.examples.kafka.jsonposfanout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableBinding
public class JsonposfanoutApplication {

	public static void main(String[] args) {
		SpringApplication.run(JsonposfanoutApplication.class, args);
	}
}