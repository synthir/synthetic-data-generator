package no.uit.syntHIR.syntheticFHIRData.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"no.uit.syntHIR.syntheticFHIRData.endpoint", "no.uit.syntHIR.syntheticFHIRData.engine"})
public class SyntheticFhirDataServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SyntheticFhirDataServicesApplication.class, args);
	}

}
