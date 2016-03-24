package uk.co.zoneofavoidance.my.tasks.controllers.rest.response;

public class ProjectSummaryResponse {

   private final long id;

   private final String name;

   private final String description;

   private final int numberOfOpenTasks;

   private final String href;

   public ProjectSummaryResponse(final long id, final String name, final String description, final int numberOfOpenTasks, final String href) {
      this.id = id;
      this.name = name;
      this.description = description;
      this.numberOfOpenTasks = numberOfOpenTasks;
      this.href = href;
   }

   public long getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public String getDescription() {
      return description;
   }

   public int getNumberOfOpenTasks() {
      return numberOfOpenTasks;
   }

   public String getHref() {
      return href;
   }

}
