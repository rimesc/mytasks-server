package uk.co.zoneofavoidance.my.tasks.rest.response;

/**
 * REST response containing the details of a project.
 */
public class ProjectResponse {

   private final long id;

   private final String name;

   private final String description;

   private final int numberOfOpenTasks;

   private final String href;

   /**
    * @param id ID of the project
    * @param name name of the project
    * @param description description of the project
    * @param numberOfOpenTasks number of open tasks in the project
    * @param href path to this project resource
    */
   public ProjectResponse(final long id, final String name, final String description, final int numberOfOpenTasks, final String href) {
      this.id = id;
      this.name = name;
      this.description = description;
      this.numberOfOpenTasks = numberOfOpenTasks;
      this.href = href;
   }

   /**
    * @return the ID of the project
    */
   public long getId() {
      return id;
   }

   /**
    * @return the name of the project
    */
   public String getName() {
      return name;
   }

   /**
    * @return a description of the project
    */
   public String getDescription() {
      return description;
   }

   /**
    * @return the number of open tasks in the project
    */
   public int getNumberOfOpenTasks() {
      return numberOfOpenTasks;
   }

   /**
    * @return the path to the project resource
    */
   public String getHref() {
      return href;
   }

}
