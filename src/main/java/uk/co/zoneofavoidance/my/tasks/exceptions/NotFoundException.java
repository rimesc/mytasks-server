package uk.co.zoneofavoidance.my.tasks.exceptions;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Error raised when a requested object is not found.
 */
@ResponseStatus(NOT_FOUND)
public class NotFoundException extends RuntimeException {

   /**
    * @param objectType type of object requested
    */
   public NotFoundException(final String objectType) {
      super("The requested " + objectType + " could not be found.");
   }

}
