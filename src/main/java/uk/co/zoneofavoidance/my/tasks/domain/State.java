package uk.co.zoneofavoidance.my.tasks.domain;

import static java.util.Arrays.asList;

import java.util.List;

/**
 * The state of a task.
 */
public enum State {

   /** Task has yet to be started. */
   TO_DO,

   /** Task is being actively worked on. */
   IN_PROGRESS,

   /** Task has been started, but is not being actively worked on. */
   ON_HOLD,

   /** Task has been completed. */
   DONE;

   /**
    * Returns a list of common transitions from this state. Common transitions
    * are:
    * <ul>
    * <li>{@link #TO_DO} &rarr; {@link #IN_PROGRESS}
    * <li>{@link #IN_PROGRESS} &rarr; {@link #ON_HOLD}
    * <li>{@link #ON_HOLD} &rarr; {@link #IN_PROGRESS}
    * <li>{@link #IN_PROGRESS} &rarr; {@link #DONE}
    * </ul>
    *
    * @return list of states which commonly follow this state
    */
   public List<State> commonTransitions() {
      switch (this) {
         case TO_DO:
            return asList(IN_PROGRESS);
         case IN_PROGRESS:
            return asList(ON_HOLD, DONE);
         case ON_HOLD:
            return asList(IN_PROGRESS);
         case DONE:
            return asList();
         default:
            throw new AssertionError("Unhandled state: " + name());
      }
   }

}
