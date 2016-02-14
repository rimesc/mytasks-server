package uk.co.zoneofavoidance.my.tasks.exceptions;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(NOT_FOUND)
public class NotFoundException extends RuntimeException {

   public NotFoundException(final String objectType) {
      super("The requested " + objectType + " could not be found.");
   }

}
