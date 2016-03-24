package uk.co.zoneofavoidance.my.tasks.controllers.rest.response;

import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "code", "message" })
public class BindingErrorResponse {

   private final String code;
   private final String message;

   protected BindingErrorResponse(final String code, final String message) {
      this.code = code;
      this.message = message;
   }

   public String getCode() {
      return code;
   }

   public String getMessage() {
      return message;
   }

   public static BindingErrorResponse create(final ObjectError error) {
      return new BindingErrorResponse(error.getCode(), error.getDefaultMessage());
   }

}