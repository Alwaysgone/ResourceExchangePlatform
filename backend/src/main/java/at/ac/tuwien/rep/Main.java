package at.ac.tuwien.rep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@ComponentScan(basePackages="at.ac.tuwien.rep")
@EnableAutoConfiguration
@EnableMongoRepositories(basePackages="at.ac.tuwien.rep.dao")
public class Main {
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}
