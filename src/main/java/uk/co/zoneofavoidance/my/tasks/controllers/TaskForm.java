package uk.co.zoneofavoidance.my.tasks.controllers;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import uk.co.zoneofavoidance.my.tasks.domain.Priority;

/**
 * Bean backing the new task form.
 */
public class TaskForm {

   @NotNull
   private Long project;

   @Length(min = 1, max = 255)
   private String summary;

   private String description;

   @NotNull
   private Priority priority;

   public TaskForm() {
   }

   public TaskForm(final Long project, final String summary, final String description, final Priority priority) {
      this.project = project;
      this.summary = summary;
      this.description = description;
      this.priority = priority;
   }

   public Long getProject() {
      return project;
   }

   public String getSummary() {
      return summary;
   }

   public String getDescription() {
      return description;
   }

   public Priority getPriority() {
      return priority;
   }

   public void setProject(final Long project) {
      this.project = project;
   }

   public void setSummary(final String summary) {
      this.summary = summary;
   }

   public void setDescription(final String description) {
      this.description = description;
   }

   public void setPriority(final Priority priority) {
      this.priority = priority;
   }

}
