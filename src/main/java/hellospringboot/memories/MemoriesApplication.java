package hellospringboot.memories;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("hellospringboot.memories.repository")
@EntityScan("hellospringboot.memories.model")
@SpringBootApplication
public class MemoriesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemoriesApplication.class, args);
	}

}
