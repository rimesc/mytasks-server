package uk.co.zoneofavoidance.my.tasks.controllers.rest.response;

import java.util.Date;

import uk.co.zoneofavoidance.my.tasks.domain.Priority;
import uk.co.zoneofavoidance.my.tasks.domain.State;

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

   public long getId() {
      return id;
   }

   public String getSummary() {
      return summary;
   }

   public String getDescription() {
      return description;
   }

   public Priority getPriority() {
      return priority;
   }

   public State getState() {
      return state;
   }

   public Date getCreated() {
      return created;
   }

   public Date getUpdated() {
      return updated;
   }

   public boolean isModified() {
      return getUpdated().after(getCreated());
   }

   public long getProject() {
      return project;
   }

   public String getHref() {
      return href;
   }

}
