package uk.co.zoneofavoidance.my.tasks.response;

import java.util.List;

/**
 * REST response containing a list of tasks.
 */
public class TasksResponse {

   private final List<TaskJson> tasks;

   /**
    * @param tasks list of individual task resources
    */
   public TasksResponse(final List<TaskJson> tasks) {
      this.tasks = tasks;
   }

   /**
    * @return the list of individual task responses
    */
   public List<TaskJson> getTasks() {
      return tasks;
   }

}
