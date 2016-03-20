package uk.co.zoneofavoidance.my.tasks.controllers.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectSummaryResponse {

   @JsonProperty
   private final long id;

   @JsonProperty
   private final String name;

   @JsonProperty
   private final String description;

   @JsonProperty
   private final int numberOfOpenTasks;

   @JsonProperty
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
