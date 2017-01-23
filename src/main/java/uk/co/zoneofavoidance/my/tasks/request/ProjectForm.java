package uk.co.zoneofavoidance.my.tasks.request;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import uk.co.zoneofavoidance.my.tasks.request.validation.Create;

/**
 * Bean backing the new/update project forms.
 */
public class ProjectForm {

   @NotNull(groups = Create.class)
   @Length(min = 1, max = 255)
   private String name;

   private String description;

   /**
    * @return the name of the project
    */
   public String getName() {
      return name;
   }

   /**
    * @return a description of the project
    */
   public String getDescription() {
      return description;
   }

   /**
    * @param name name of the project
    */
   public void setName(final String name) {
      this.name = name;
   }

   /**
    * @param description description of the project
    */
   public void setDescription(final String description) {
      this.description = description;
   }

}
