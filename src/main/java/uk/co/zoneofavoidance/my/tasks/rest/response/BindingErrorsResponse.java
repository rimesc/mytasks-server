package uk.co.zoneofavoidance.my.tasks.rest.response;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.validation.BindingResult;

/**
 * REST response containing details of form binding errors.
 */
public class BindingErrorsResponse {

   private final List<ErrorResponse> errors;

   private BindingErrorsResponse(final List<ErrorResponse> errors) {
      this.errors = errors;
   }

   /**
    * @return a list of the individual binding errors
    */
   public List<ErrorResponse> getErrors() {
      return errors;
   }

   /**
    * Creates a REST response from binding results.
    * 
    * @param bindingResult the binding results
    * @return a REST response containing details of the binding errors
    */
   public static BindingErrorsResponse create(final BindingResult bindingResult) {
      final Stream<FieldErrorResponse> fieldErrors = bindingResult.getFieldErrors().stream().map(FieldErrorResponse::create);
      final Stream<ErrorResponse> globalErrors = bindingResult.getGlobalErrors().stream().map(ErrorResponse::create);
      return new BindingErrorsResponse(Stream.concat(fieldErrors, globalErrors).collect(toList()));
   }

}
