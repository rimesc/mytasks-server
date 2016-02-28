package uk.co.zoneofavoidance.my.tasks.controllers;

import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static uk.co.zoneofavoidance.my.tasks.controllers.TaskSelection.ALL;
import static uk.co.zoneofavoidance.my.tasks.controllers.TaskSelection.CLOSED;
import static uk.co.zoneofavoidance.my.tasks.controllers.TaskSelection.OPEN;
import static uk.co.zoneofavoidance.my.tasks.domain.State.DONE;
import static uk.co.zoneofavoidance.my.tasks.domain.State.IN_PROGRESS;
import static uk.co.zoneofavoidance.my.tasks.domain.State.ON_HOLD;
import static uk.co.zoneofavoidance.my.tasks.domain.State.TO_DO;

import org.junit.Test;

import static org.junit.Assert.assertThat;

import uk.co.zoneofavoidance.my.tasks.domain.State;

/**
 * Unit tests for {@link TaskSelection}.
 */
public class TaskSelectionTest {

   @Test
   public void getOpenStatesIncludesToDoInProgressAndOnHold() {
      assertThat(OPEN.getStates(), arrayContainingInAnyOrder(TO_DO, IN_PROGRESS, ON_HOLD));
   }

   @Test
   public void getClosedStatesIncludesDone() {
      assertThat(CLOSED.getStates(), arrayContainingInAnyOrder(DONE));
   }

   @Test
   public void getAllStatesIncludesAllStates() {
      assertThat(ALL.getStates(), arrayContainingInAnyOrder(State.values()));
   }

}
