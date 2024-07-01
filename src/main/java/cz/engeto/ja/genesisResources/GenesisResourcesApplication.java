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

@SpringBootApplication
@ComponentScan(basePackages = {"cz.engeto.ja.genesisResources"})
public class GenesisResourcesApplication {
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(GenesisResourcesApplication.class);
		app.addListeners(new ApplicationStartingListener());
		app.run(args);
	}

	@Component
	public static class ApplicationStartingListener implements ApplicationListener<ApplicationStartingEvent> {
		@Override
		public void onApplicationEvent(ApplicationStartingEvent event) {
			AppLogger.log("Genesis Resources Application starting.");
		}
	}

	@Component
	public static class ApplicationEventListener implements ApplicationListener<ContextRefreshedEvent> {
		@Override
		public void onApplicationEvent(ContextRefreshedEvent event) {
			AppLogger.log("Genesis Resources Application started.");
		}
	}

	@Component
	public static class ApplicationShutdownListener implements ApplicationListener<ContextClosedEvent> {
		@Override
		public void onApplicationEvent(ContextClosedEvent event) {
			AppLogger.log("Genesis Resources Application shutting down.");
		}
	}
}
