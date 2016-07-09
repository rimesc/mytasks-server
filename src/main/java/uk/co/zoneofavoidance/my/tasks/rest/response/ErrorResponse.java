package uk.co.zoneofavoidance.my.tasks.rest.response;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * REST response containing details of general errors.
 */
@JsonPropertyOrder({ "code", "message" })
public class ErrorResponse {

   private final String code;
   private final String message;

   /**
    * Constructs a new response.
    *
    * @param code error code
    * @param message error message
    */
   public ErrorResponse(final String code, final String message) {
      this.code = code;
      this.message = message;
   }

   /**
    * @return the code of the error
    */
   public String getCode() {
      return code;
   }

   /**
    * @return a message describing the error
    */
   public String getMessage() {
      return message;
   }

   /**
    * Creates an error response from a binding error.
    *
    * @param error binding error
    * @return an error response
    */
   public static ErrorResponse create(final ObjectError error) {
      return new ErrorResponse(error.getCode(), error.getDefaultMessage());
   }

   /**
    * Creates an error response with an HTTP status and a message.
    *
    * @param code code of the error
    * @param message description of the error
    * @return an error response
    */
   public static ErrorResponse create(final HttpStatus code, final String message) {
      return new ErrorResponse(code.getReasonPhrase(), message);
   }

}