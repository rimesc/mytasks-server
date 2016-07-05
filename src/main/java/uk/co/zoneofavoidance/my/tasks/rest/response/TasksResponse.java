package uk.co.zoneofavoidance.my.tasks.rest.response;

import java.util.List;

public class TasksResponse {

   private final List<TaskResponse> tasks;

   public TasksResponse(final List<TaskResponse> tasks) {
      this.tasks = tasks;
   }

   public List<TaskResponse> getTasks() {
      return tasks;
   }

}
