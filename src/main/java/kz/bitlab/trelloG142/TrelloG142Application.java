package kz.bitlab.trelloG142;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TrelloG142Application {

	public static void main(String[] args) {
		SpringApplication.run(TrelloG142Application.class, args);
	}

}
