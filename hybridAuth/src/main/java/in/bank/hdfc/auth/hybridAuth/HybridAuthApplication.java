package in.bank.hdfc.auth.hybridAuth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HybridAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(HybridAuthApplication.class, args);
	}

}
