package uk.co.zoneofavoidance.my.tasks.rest.request;

import org.hibernate.validator.constraints.Length;

import uk.co.zoneofavoidance.my.tasks.domain.Priority;
import uk.co.zoneofavoidance.my.tasks.domain.State;

public class UpdateTaskRequest {

   @Length(min = 1, max = 255)
   private String summary;

   private String description;

   private Priority priority;

   private State state;

   public UpdateTaskRequest() {
   }

   public State getState() {
      return state;
   }

   public void setState(final State state) {
      this.state = state;
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
