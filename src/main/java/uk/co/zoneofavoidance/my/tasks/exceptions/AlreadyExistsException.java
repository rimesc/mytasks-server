package uk.co.zoneofavoidance.my.tasks.exceptions;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Error raised when attempting to create an object that already exists.
 */
@ResponseStatus(BAD_REQUEST)
public class AlreadyExistsException extends RuntimeException {

   /**
    * @param objectType type of the object
    * @param objectName name of the object
    */
   public AlreadyExistsException(final String objectType, final String objectName) {
      super("The " + objectType + "'" + objectName + "' already exists.");
   }

}
