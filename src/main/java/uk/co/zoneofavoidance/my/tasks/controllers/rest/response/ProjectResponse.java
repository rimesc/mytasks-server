package uk.co.zoneofavoidance.my.tasks.controllers.rest.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class ProjectResponse {

   @JsonUnwrapped
   private final Summary summary;

   private final String readMe;

   public static ProjectResponse create(final long id, final String name, final String description, final int numberOfOpenTasks, final String href, final String readMe) {
      return new ProjectResponse(new Summary(id, name, description, numberOfOpenTasks, href), readMe);
   }

   public ProjectResponse(final Summary summary, final String readMe) {
      this.summary = summary;
      this.readMe = readMe;
   }

   public String getReadMe() {
      return readMe;
   }

   public static class Summary {

      private final long id;

      private final String name;

      private final String description;

      private final int numberOfOpenTasks;

      private final String href;

      public Summary(final long id, final String name, final String description, final int numberOfOpenTasks, final String href) {
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

}
