package uk.co.zoneofavoidance.my.tasks.controllers.rest.response;

import org.springframework.validation.FieldError;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "field", "code", "message" })
public class FieldErrorResponse extends BindingErrorResponse {

   private final String field;

   public FieldErrorResponse(final String field, final String code, final String message) {
      super(code, message);
      this.field = field;
   }

   public String getField() {
      return field;
   }

   public static FieldErrorResponse create(final FieldError error) {
      return new FieldErrorResponse(error.getField(), error.getCode(), error.getDefaultMessage());
   }

}
