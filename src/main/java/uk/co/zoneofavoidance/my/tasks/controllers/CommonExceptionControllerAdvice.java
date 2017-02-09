package uk.co.zoneofavoidance.my.tasks.controllers;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import uk.co.zoneofavoidance.my.tasks.exceptions.NotFoundException;
import uk.co.zoneofavoidance.my.tasks.exceptions.PermissionDeniedException;
import uk.co.zoneofavoidance.my.tasks.response.BindingErrorsResponse;
import uk.co.zoneofavoidance.my.tasks.response.ErrorResponse;

/**
 * REST controller for the projects API.
 */
@ControllerAdvice
public class CommonExceptionControllerAdvice {

   /**
    * Error handler for invalid forms.
    *
    * @param ex exception
    * @return a REST response containing details of the error
    */
   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<BindingErrorsResponse> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex) {
      return new ResponseEntity<>(BindingErrorsResponse.create(ex.getBindingResult()), BAD_REQUEST);
   }

   /**
    * Error handler for invalid forms.
    *
    * @param ex exception
    * @return a REST response containing details of the error
    */
   @ExceptionHandler(BindException.class)
   public ResponseEntity<BindingErrorsResponse> handleBindingError(final BindException ex) {
      return new ResponseEntity<>(BindingErrorsResponse.create(ex.getBindingResult()), BAD_REQUEST);
   }

   /**
    * Error handler for when a resource is not found.
    *
    * @param ex exception
    * @return a REST response containing details of the error
    */
   @ExceptionHandler(NotFoundException.class)
   public ResponseEntity<ErrorResponse> handleNotFound(final NotFoundException ex) {
      return new ResponseEntity<>(ErrorResponse.create(NOT_FOUND, ex.getMessage()), NOT_FOUND);
   }

   /**
    * Error handler for when the user doesn't have permission to view a
    * resource.
    *
    * @param ex exception
    * @return a REST response containing details of the error
    */
   @ExceptionHandler(PermissionDeniedException.class)
   public ResponseEntity<ErrorResponse> handleForbidden(final PermissionDeniedException ex) {
      return new ResponseEntity<>(ErrorResponse.create(FORBIDDEN, ex.getMessage()), FORBIDDEN);
   }

}
