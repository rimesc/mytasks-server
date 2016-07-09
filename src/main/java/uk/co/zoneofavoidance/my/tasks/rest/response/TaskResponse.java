package uk.co.zoneofavoidance.my.tasks.rest.response;

import java.util.Date;

import uk.co.zoneofavoidance.my.tasks.domain.Priority;
import uk.co.zoneofavoidance.my.tasks.domain.State;

/**
 * REST response containing the details of a task.
 */
public class TaskResponse {

   private final long id;

   private final String summary;

   private final String description;

   private final Priority priority;

   private final State state;

   private final Date created;

   private final Date updated;

   private final long project;

   private final String href;

   /**
    * @param id ID of the task
    * @param summary summary of the task
    * @param description description of the task
    * @param priority priority of the task
    * @param state current state of the task
    * @param created date when this task was created
    * @param updated date when this task was last modified
    * @param project ID of this task's parent project
    * @param href path to this task resource
    */
   public TaskResponse(final long id, final String summary, final String description, final Priority priority, final State state, final Date created, final Date updated, final long project, final String href) {
      this.id = id;
      this.summary = summary;
      this.description = description;
      this.priority = priority;
      this.state = state;
      this.created = created;
      this.updated = updated;
      this.project = project;
      this.href = href;
   }

   /**
    * @return the ID of the task
    */
   public long getId() {
      return id;
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
    * @return the state of the task
    */
   public State getState() {
      return state;
   }

   /**
    * @return the date when the task was created
    */
   public Date getCreated() {
      return created;
   }

   /**
    * @return the date when the task was last modified
    */
   public Date getUpdated() {
      return updated;
   }

   /**
    * @return <code>true</code> if the task has been modified, otherwise
    *         <code>false</code>
    */
   public boolean isModified() {
      return getUpdated().after(getCreated());
   }

   /**
    * @return the ID of the task's parent project
    */
   public long getProject() {
      return project;
   }

   /**
    * @return the path to this task resource
    */
   public String getHref() {
      return href;
   }

}
