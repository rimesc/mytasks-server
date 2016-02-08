package uk.co.zoneofavoidance.my.tasks.controllers;

import org.hibernate.validator.constraints.Length;

/**
 * Bean backing the new project form.
 */
public class ProjectForm {

   @Length(min = 1, max = 255)
   private String name;

   private String description;

   private String readMe;

   public ProjectForm() {
   }

   public ProjectForm(final String name, final String description, final String readMe) {
      this.name = name;
      this.description = description;
      this.readMe = readMe;
   }

   public String getName() {
      return name;
   }

   public String getDescription() {
      return description;
   }

   public String getReadMe() {
      return readMe;
   }

   public void setName(final String name) {
      this.name = name;
   }

   public void setDescription(final String description) {
      this.description = description;
   }

   public void setDocumentation(final String documentation) {
      this.readMe = documentation;
   }
}
