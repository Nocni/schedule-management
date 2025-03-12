package rs.raf.RasporedService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class RasporedServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RasporedServiceApplication.class, args);
	}

}
