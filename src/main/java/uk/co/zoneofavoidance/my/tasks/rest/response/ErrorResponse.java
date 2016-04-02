package uk.co.zoneofavoidance.my.tasks.rest.response;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "code", "message" })
public class ErrorResponse {

   private final String code;
   private final String message;

   public ErrorResponse(final String code, final String message) {
      this.code = code;
      this.message = message;
   }

   public String getCode() {
      return code;
   }

   public String getMessage() {
      return message;
   }

   public static ErrorResponse create(final ObjectError error) {
      return new ErrorResponse(error.getCode(), error.getDefaultMessage());
   }

   public static ErrorResponse create(final HttpStatus code, final String message) {
      return new ErrorResponse(code.getReasonPhrase(), message);
   }

}