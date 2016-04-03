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

   public ProjectForm() {
   }

   public ProjectForm(final String name, final String description) {
      this.name = name;
      this.description = description;
   }

   public String getName() {
      return name;
   }

   public String getDescription() {
      return description;
   }

   public void setName(final String name) {
      this.name = name;
   }

   public void setDescription(final String description) {
      this.description = description;
   }

}