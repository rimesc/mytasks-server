package uk.co.zoneofavoidance.my.tasks.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * JSON for a REST API resource. Wraps some data (the payload) with a path
 * relative to the context root.
 *
 * @param <T> type of the payload
 */
@JsonPropertyOrder({ "payload", "href" })
public class ResourceJson<T> {

   private final T payload;

   private final String href;

   /**
    * @param payload the actual data
    * @param href path of this resource relative to the context root
    */
   public ResourceJson(final T payload, final String href) {
      this.payload = payload;
      this.href = href;
   }

   /**
    * @return the payload
    */
   @JsonUnwrapped
   public T getPayload() {
      return payload;
   }

   /**
    * @return the path of this resource relative to the context root
    */
   public String getHref() {
      return href;
   }

}
