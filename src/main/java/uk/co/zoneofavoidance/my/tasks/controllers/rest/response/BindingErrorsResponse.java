package uk.co.zoneofavoidance.my.tasks.controllers.rest.response;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.validation.BindingResult;

public class BindingErrorsResponse {

   private final List<ErrorResponse> _errors;

   private BindingErrorsResponse(final List<ErrorResponse> errors) {
      _errors = errors;
   }

   public List<ErrorResponse> getErrors() {
      return _errors;
   }

   public static BindingErrorsResponse create(final BindingResult bindingResult) {
      final Stream<FieldErrorResponse> fieldErrors = bindingResult.getFieldErrors().stream().map(FieldErrorResponse::create);
      final Stream<ErrorResponse> globalErrors = bindingResult.getGlobalErrors().stream().map(ErrorResponse::create);
      return new BindingErrorsResponse(Stream.concat(fieldErrors, globalErrors).collect(toList()));
   }

}
