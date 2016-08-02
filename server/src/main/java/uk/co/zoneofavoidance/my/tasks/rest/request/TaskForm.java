package uk.co.zoneofavoidance.my.tasks.rest.request;

import java.util.Collections;
import java.util.Set;

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

   @NotNull
   private Set<String> tags = Collections.emptySet();

   /**
    * @return the ID of the parent project
    */
   public Long getProject() {
      return project;
   }

   /**
    * @return the summary of the task
    */
   public String getSummary() {
      return summary;
   }

   /**
    * @return a description of the task
    */
   public String getDescription() {
      return description;
   }

   /**
    * @return the priority of the task
    */
   public Priority getPriority() {
      return priority;
   }

   /**
    * @return the tags for the task
    */
   public Set<String> getTags() {
      return tags;
   }

   /**
    * @param project ID of the parent project
    */
   public void setProject(final Long project) {
      this.project = project;
   }

   /**
    * @param summary summary of the task
    */
   public void setSummary(final String summary) {
      this.summary = summary;
   }

   /**
    * @param description description of the task
    */
   public void setDescription(final String description) {
      this.description = description;
   }

   /**
    * @param priority priority of the task
    */
   public void setPriority(final Priority priority) {
      this.priority = priority;
   }

   /**
    * @param tags tags for the new task
    */
   public void setTags(final Set<String> tags) {
      this.tags = tags;
   }

}
