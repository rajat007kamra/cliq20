package selenium;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @summary This is an entry point for Selenium core library
 * @author Manoj.Jain
 */
@SpringBootApplication
public class Application {

	public static void main(final String[] args) throws Exception {
		PropertyConfigurator.configure("src/main/resources/log4j.properties");
		SpringApplication.run(Application.class, args);
	}
}
