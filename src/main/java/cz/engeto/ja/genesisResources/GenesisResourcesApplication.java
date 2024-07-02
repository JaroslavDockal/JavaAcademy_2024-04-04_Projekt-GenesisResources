// Uživatelské jméno na discordu:   'jaroslav.dockal'

package cz.engeto.ja.genesisResources;

import cz.engeto.ja.genesisResources.util.AppLogger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Main entry point for Genesis Resources Application.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"cz.engeto.ja.genesisResources"})
public class GenesisResourcesApplication {

	/**
	 * Main method to start the application.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(GenesisResourcesApplication.class);
		app.addListeners(new ApplicationStartingListener());
		app.run(args);
	}

	/**
	 * Listener for application starting event.
	 */
	@Component
	public static class ApplicationStartingListener implements ApplicationListener<ApplicationStartingEvent> {
		@Override
		public void onApplicationEvent(ApplicationStartingEvent event) {
			AppLogger.info("Genesis Resources Application starting.");
		}
	}

	/**
	 * Listener for application refreshed event.
	 */
	@Component
	public static class ApplicationEventListener implements ApplicationListener<ContextRefreshedEvent> {
		@Override
		public void onApplicationEvent(ContextRefreshedEvent event) {
			AppLogger.info("Genesis Resources Application started.");
		}
	}

	/**
	 * Listener for application shutdown event.
	 */
	@Component
	public static class ApplicationShutdownListener implements ApplicationListener<ContextClosedEvent> {
		@Override
		public void onApplicationEvent(ContextClosedEvent event) {
			AppLogger.info("Genesis Resources Application shutting down.");
		}
	}
}
