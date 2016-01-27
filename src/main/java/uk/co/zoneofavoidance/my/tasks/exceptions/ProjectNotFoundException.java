package uk.co.zoneofavoidance.my.tasks.exceptions;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(NOT_FOUND)
public class ProjectNotFoundException extends RuntimeException {

   public ProjectNotFoundException() {
      super("The requested project could not be found.");
   }

}
