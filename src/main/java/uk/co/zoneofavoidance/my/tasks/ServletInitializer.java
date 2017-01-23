package uk.co.zoneofavoidance.my.tasks;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Initialises the application when running in a servlet container.
 */
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
   protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
		return application.sources(MyTasksApplication.class);
	}

}
