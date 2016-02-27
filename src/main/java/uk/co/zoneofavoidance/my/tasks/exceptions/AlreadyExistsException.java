package uk.co.zoneofavoidance.my.tasks.exceptions;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(BAD_REQUEST)
public class AlreadyExistsException extends RuntimeException {

   public AlreadyExistsException(final String objectType, final String objectName) {
      super("The " + objectType + "'" + objectName + "' already exists.");
   }

}
