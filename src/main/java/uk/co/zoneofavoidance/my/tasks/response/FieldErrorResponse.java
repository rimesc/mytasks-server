package uk.co.zoneofavoidance.my.tasks.response;

import org.springframework.validation.FieldError;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * REST response containing details of form field errors.
 */
@JsonPropertyOrder({ "field", "code", "message" })
public class FieldErrorResponse extends ErrorResponse {

   private final String field;

   /**
    * Constructs a new response.
    *
    * @param field name of the field in error
    * @param code error code
    * @param message error message
    */
   public FieldErrorResponse(final String field, final String code, final String message) {
      super(code, message);
      this.field = field;
   }

   /**
    * @return the name of the field in error
    */
   public String getField() {
      return field;
   }

   /**
    * Creates an error response from a field validation error.
    *
    * @param error field validation error
    * @return an error response
    */
   public static FieldErrorResponse create(final FieldError error) {
      return new FieldErrorResponse(error.getField(), error.getCode(), error.getDefaultMessage());
   }

}
