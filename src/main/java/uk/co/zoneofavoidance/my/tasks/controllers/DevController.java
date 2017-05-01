package uk.co.zoneofavoidance.my.tasks.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static uk.co.zoneofavoidance.my.tasks.controllers.DevController.BASE_PATH;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.co.zoneofavoidance.my.tasks.SampleData;

/**
 * REST controller that provides additional features in development mode.
 */
@RestController
@RequestMapping(BASE_PATH)
@Profile("dev")
public class DevController {

   static final String BASE_PATH = "/api/dev";

   private final SampleData sampleData;

   @Autowired
   public DevController(final SampleData sampleData) {
      this.sampleData = sampleData;
   }

   /**
    * End-point for resetting the sample data. Used by the client end-to-end
    * tests.
    *
    * @throws Exception if something goes wrong
    */
   @RequestMapping(path = "reset", method = POST)
   public void reset() throws Exception {
      sampleData.run();
   }

}
