package uk.co.zoneofavoidance.my.tasks.rest.request;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Bean backing the new project form.
 */
public class ProjectForm {

   @NotEmpty
   @Length(min = 1, max = 255)
   private String name;

   private String description;

   /**
    * For the framework.
    */
   public ProjectForm() {
   }

   /**
    * @param name name of the project
    * @param description description of the project
    */
   public ProjectForm(final String name, final String description) {
      this.name = name;
      this.description = description;
   }

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
