package uk.co.zoneofavoidance.my.tasks.response;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import uk.co.zoneofavoidance.my.tasks.domain.Priority;
import uk.co.zoneofavoidance.my.tasks.domain.State;

/**
 * REST response containing the details of a task.
 */
@JsonPropertyOrder({ "id", "summary", "priority", "state", "created", "updated", "tags", "notes", "project" })
public class TaskJson {

   private final long id;

   private final String summary;

   private final Priority priority;

   private final State state;

   private final Date created;

   private final Date updated;

   private final List<String> tags;

   private final NotesJson notes;

   private final BasicProjectJson project;

   /**
    * @param id ID of the task
    * @param summary summary of the task
    * @param priority priority of the task
    * @param state current state of the task
    * @param tags tags associated with the task
    * @param created date when the task was created
    * @param updated date when the task was last modified
    * @param notes description of the task
    * @param project ID of the task's parent project
    */
   public TaskJson(final long id, final String summary, final Priority priority, final State state, final List<String> tags, final Date created, final Date updated, final NotesJson notes, final BasicProjectJson project) {
      this.id = id;
      this.summary = summary;
      this.priority = priority;
      this.state = state;
      this.tags = tags;
      this.created = created;
      this.updated = updated;
      this.notes = notes;
      this.project = project;
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
    * @return the list of tags associated with the task
    */
   public List<String> getTags() {
      return tags;
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
    * @return a description of the task
    */
   public NotesJson getNotes() {
      return notes;
   }

   /**
    * @return the task's parent project
    */
   public BasicProjectJson getProject() {
      return project;
   }

}
