package fr.labri.progress.comet.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySources({
		@PropertySource(ignoreResourceNotFound = true, value = { "application.properties" }),
		@PropertySource("classpath:application-defaults.properties") })
public class SpringWebRequestConfiguration {

	@Bean(name = "apiRoot")
	public String getApiRoot(@Value("${apiRoot}") String value) {
		return value;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
