package uk.co.zoneofavoidance.my.tasks.rest.response;

import java.util.List;

/**
 * REST response containing a list of tasks.
 */
public class TasksResponse {

   private final List<TaskResponse> tasks;

   /**
    * @param tasks list of individual task resources
    */
   public TasksResponse(final List<TaskResponse> tasks) {
      this.tasks = tasks;
   }

   /**
    * @return the list of individual task responses
    */
   public List<TaskResponse> getTasks() {
      return tasks;
   }

}
