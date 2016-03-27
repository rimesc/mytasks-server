package uk.co.zoneofavoidance.my.tasks.controllers.rest.request;

import javax.validation.constraints.NotNull;

import uk.co.zoneofavoidance.my.tasks.domain.State;

public class TaskStateRequest {

   @NotNull
   private State state;

   public TaskStateRequest() {
   }

   public TaskStateRequest(final State state) {
      this.state = state;
   }

   public State getState() {
      return state;
   }

   public void setState(final State state) {
      this.state = state;
   }

}
