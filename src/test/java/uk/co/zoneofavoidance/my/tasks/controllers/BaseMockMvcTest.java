package uk.co.zoneofavoidance.my.tasks.controllers;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import uk.co.zoneofavoidance.my.tasks.MyTasksApplication;
import uk.co.zoneofavoidance.my.tasks.security.AuthenticatedUser;

/**
 * Integration test using {@link MockMvc}.
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration(classes = { BaseMockMvcTest.Config.class })
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class BaseMockMvcTest {

   static final String TEST_USER = "test_user";

   @Autowired
   private WebApplicationContext webAppContext;

   private MockMvc mockMvc;

   @Before
   public void setUp() throws Exception {
      mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
   }

   MockMvc mockMvc() {
      return mockMvc;
   }

   /**
    * Hook from which to hang an import of the main configuration.
    */
   @Configuration
   @Import(MyTasksApplication.class)
   static class Config {
      @Bean
      public AuthenticatedUser authenticatedUser() {
         return new MutableAuthenticatedUser(TEST_USER);
      }
   }

   /**
    * Mutable implementation for testing.
    */
   static final class MutableAuthenticatedUser implements AuthenticatedUser {

      private String id;

      private MutableAuthenticatedUser(final String id) {
         this.id = id;
      }

      @Override
      public String getId() {
         return id;
      }

      public void setId(final String id) {
         this.id = id;
      }

   }

}
