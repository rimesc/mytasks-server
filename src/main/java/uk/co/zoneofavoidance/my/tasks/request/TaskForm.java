package uk.co.zoneofavoidance.my.tasks.request;

import java.util.Collections;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.hibernate.validator.constraints.Length;

import uk.co.zoneofavoidance.my.tasks.domain.Priority;
import uk.co.zoneofavoidance.my.tasks.domain.State;
import uk.co.zoneofavoidance.my.tasks.request.validation.Create;

/**
 * Bean backing the new/update task forms.
 */
public class TaskForm {

   @Length(min = 1, max = 255)
   @NotNull(groups = Create.class)
   private String summary;

   @NotNull(groups = Create.class)
   private Priority priority;

   @Null(groups = Create.class)
   private State state;

   private Set<String> tags = Collections.emptySet();

   /**
    * @return the summary of the task
    */
   public String getSummary() {
      return summary;
   }

   /**
    * @return the priority of the task
    */
   public Priority getPriority() {
      return priority;
   }

   /**
    * @return the new state of the task
    */
   public State getState() {
      return state;
   }

   /**
    * @return the tags for the task
    */
   public Set<String> getTags() {
      return tags;
   }

   /**
    * @param summary summary of the task
    */
   public void setSummary(final String summary) {
      this.summary = summary;
   }

   /**
    * @param priority priority of the task
    */
   public void setPriority(final Priority priority) {
      this.priority = priority;
   }

   /**
    * @param state new state of the task
    */
   public void setState(final State state) {
      this.state = state;
   }

   /**
    * @param tags tags for the new task
    */
   public void setTags(final Set<String> tags) {
      this.tags = tags;
   }

}
