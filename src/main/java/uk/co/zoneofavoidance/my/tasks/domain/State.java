package uk.co.zoneofavoidance.my.tasks.domain;

import uk.co.zoneofavoidance.my.tasks.util.EnumUtils;

public enum State {

   OPEN, TO_DO, IN_PROGRESS, DONE, ON_HOLD, CLOSED;

   @Override
   public String toString() {
      return EnumUtils.toString(name());
   }

}
