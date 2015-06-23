package fr.labri.progress.comet.conf;

import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Scope;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

import fr.labri.progress.comet.service.WorkerMessageService;

/**
 * this class is responsible for configuring spring context and repositories
 * 
 * @author nherbaut
 *
 */
@Configuration
@ComponentScan(basePackages = { "fr.labri.progress.comet.service",
		"fr.labri.progress.comet.repository", "fr.labri.progress.comet.conf" })
@EnableJpaRepositories("fr.labri.progress.comet.repository")
@Import({ RabbitMqConfiguration.class, SpringWebRequestConfiguration.class })
public class SpringConfiguration {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SpringConfiguration.class);

	@Bean
	public ObjectMapper getObjectMapper() {
		ObjectMapper om = new ObjectMapper();
		AnnotationIntrospector introspector = new JaxbAnnotationIntrospector(
				TypeFactory.defaultInstance());
		om.setAnnotationIntrospector(introspector);
		return om;

	}

	// @Inject
	// WorkerMessageService wms;
	//
	// @PostConstruct
	// public void setupQueue() {
	// try {
	// wms.setupResultQueue();
	// } catch (AmqpConnectException e) {
	// LOGGER.warn("failed to setup the result queue for RabbitMQ");
	// }
	// }

	@Bean(name = "transactionManager")
	@Inject
	public PlatformTransactionManager tm(EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}

	@Bean(name = "entityManagerFactory")
	@Inject
	public EntityManagerFactory emf() {

		return Persistence.createEntityManagerFactory("openLatexFactory");
	}

}
