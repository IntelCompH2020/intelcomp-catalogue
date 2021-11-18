package eu.intelcomp.catalogue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class IntelcompApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntelcompApplication.class, args);
	}

}
