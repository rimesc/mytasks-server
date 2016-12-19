package uk.co.zoneofavoidance.my.tasks.request;

import java.util.Set;

import org.hibernate.validator.constraints.Length;

import uk.co.zoneofavoidance.my.tasks.domain.Priority;
import uk.co.zoneofavoidance.my.tasks.domain.State;

/**
 * Request body for updating a task.
 */
public class UpdateTaskRequest {

   @Length(min = 1, max = 255)
   private String summary;

   private String description;

   private Priority priority;

   private State state;

   private Set<String> tags;

   /**
    * @return the new state of the task
    */
   public State getState() {
      return state;
   }

   /**
    * @return the new task summary
    */
   public String getSummary() {
      return summary;
   }

   /**
    * @return the new task description
    */
   public String getDescription() {
      return description;
   }

   /**
    * @return the new task priority
    */
   public Priority getPriority() {
      return priority;
   }

   /**
    * @return the new set of task tags
    */
   public Set<String> getTags() {
      return tags;
   }

   /**
    * @param state new state of the task
    */
   public void setState(final State state) {
      this.state = state;
   }

   /**
    * @param summary new summary of the task
    */
   public void setSummary(final String summary) {
      this.summary = summary;
   }

   /**
    * @param description new description of the task
    */
   public void setDescription(final String description) {
      this.description = description;
   }

   /**
    * @param priority new priority of the task
    */
   public void setPriority(final Priority priority) {
      this.priority = priority;
   }

   /**
    * @param tags new set of task tags
    */
   public void setTags(final Set<String> tags) {
      this.tags = tags;
   }

}
