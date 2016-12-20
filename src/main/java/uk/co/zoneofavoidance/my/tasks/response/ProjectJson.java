package uk.co.zoneofavoidance.my.tasks.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * JSON representing the details of a project.
 */
@JsonPropertyOrder({ "summary", "description", "tasks", "notes" })
public class ProjectJson {

   private final BasicProjectJson basicDetails;

   private final String description;

   private final TasksByStateJson tasksByState;

   private final NotesJson notes;

   /**
    * @param basicDetails basic details of the project
    * @param description description of the project
    * @param numberOfTasks number of tasks in the project
    * @param numberOfOpenTasks number of open tasks in the project
    * @param notes notes for the project
    */
   public ProjectJson(final BasicProjectJson basicDetails, final String description, final int numberOfTasks, final int numberOfOpenTasks, final NotesJson notes) {
      this.basicDetails = basicDetails;
      this.description = description;
      this.tasksByState = new TasksByStateJson(numberOfTasks, numberOfOpenTasks);
      this.notes = notes;
   }

   /**
    * @return the basic details of the project
    */
   @JsonUnwrapped
   public BasicProjectJson getBasicDetails() {
      return basicDetails;
   }

   /**
    * @return a description of the project
    */
   public String getDescription() {
      return description;
   }

   /**
    * @return the number of tasks in each state (open, closed, total)
    */
   public TasksByStateJson getTasks() {
      return tasksByState;
   }

   /**
    * @return the notes for the project
    */
   public NotesJson getNotes() {
      return notes;
   }

   /**
    * JSON breakdown of number of tasks by state (open, closed, total).
    */
   public static class TasksByStateJson {
      private final int total;
      private final int open;
      private final int closed;

      /**
       * @param total total number of tasks
       * @param open number of open tasks
       */
      public TasksByStateJson(final int total, final int open) {
         this.total = total;
         this.open = open;
         this.closed = total - open;
      }

      /**
       * @return the total number of tasks
       */
      public int getTotal() {
         return total;
      }

      /**
       * @return the number of open tasks
       */
      public int getOpen() {
         return open;
      }

      /**
       * @return the number of closed tasks
       */
      public int getClosed() {
         return closed;
      }

   }

}
