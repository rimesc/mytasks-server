package uk.co.zoneofavoidance.my.tasks.tests.jasmine;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Main class for Jasmine tests of client-side Angular JS.
 */
@SpringBootApplication
@Controller
@Profile("jasmine")
@SuppressWarnings("checkstyle:javadocmethod")
public class JasmineTestApplication {

   @RequestMapping("/")
   public String home() {
      return "forward:/test.html";
   }

   public static void main(final String[] args) {
      new SpringApplicationBuilder(JasmineTestApplication.class)
         .properties("server.port=9999", "security.basic.enabled=false", "spring.profiles.active=dev,jasmine")
         .run(args);
   }

}
