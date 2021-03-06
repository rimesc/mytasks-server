package uk.co.zoneofavoidance.my.tasks.controllers;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import uk.co.zoneofavoidance.my.tasks.MyTasksApplication;

/**
 * Integration test using {@link MockMvc}.
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class BaseMockMvcTest {

   @Autowired
   private WebApplicationContext webAppContext;

   private MockMvc mockMvc;

   @Before
   public void setUp() throws Exception {
      mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
   }

   /**
    * @return the mock MVC
    */
   protected MockMvc mockMvc() {
      return mockMvc;
   }

   /**
    * Hook from which to hang an import of the main configuration.
    */
   @Configuration
   @Import(MyTasksApplication.class)
   public static class Config {
   }

}
