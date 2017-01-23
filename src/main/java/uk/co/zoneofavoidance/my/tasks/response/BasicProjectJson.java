package uk.co.zoneofavoidance.my.tasks.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Basic JSON representation of a project. This minimal form is intended for
 * embedding in other documents.
 */
@JsonPropertyOrder({ "id", "name" })
public class BasicProjectJson {

   private final long id;

   private final String name;

   /**
    * @param id unique identifier for the project
    * @param name name of the project
    */
   public BasicProjectJson(final long id, final String name) {
      this.id = id;
      this.name = name;
   }

   /**
    * @return the unique identifier of the project
    */
   public long getId() {
      return id;
   }

   /**
    * @return the name of the project
    */
   public String getName() {
      return name;
   }

}
