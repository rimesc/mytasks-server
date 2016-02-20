package uk.co.zoneofavoidance.my.tasks.controllers;

import uk.co.zoneofavoidance.my.tasks.domain.State;

/**
 * Options for selecting which tasks to display.
 */
public enum TaskSelection {

   OPEN(State.TO_DO, State.IN_PROGRESS, State.ON_HOLD),

   CLOSED(State.DONE),

   ALL(State.values());

   private final State[] states;

   private TaskSelection(final State... states) {
      this.states = states;
   }

   /**
    * Returns the states included in this selection.
    *
    * @return list of included states
    */
   public State[] getStates() {
      return states;
   }

}
