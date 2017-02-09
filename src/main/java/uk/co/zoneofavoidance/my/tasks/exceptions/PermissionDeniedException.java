package uk.co.zoneofavoidance.my.tasks.exceptions;

import static org.springframework.http.HttpStatus.FORBIDDEN;

import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Error raised when a requested object is not found.
 */
@ResponseStatus(FORBIDDEN)
public class PermissionDeniedException extends RuntimeException {

   /**
    * @param objectType type of object requested
    * @param action action attempted (e.g. 'view', 'edit')
    */
   public PermissionDeniedException(final String objectType, final String action) {
      super("You don't have permission to " + action + " the requested " + objectType + ".");
   }

}
