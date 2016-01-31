package uk.co.zoneofavoidance.my.tasks.domain;

import uk.co.zoneofavoidance.my.tasks.util.EnumUtils;

public enum State {

   TO_DO, IN_PROGRESS, ON_HOLD, DONE;

   @Override
   public String toString() {
      return EnumUtils.toString(name());
   }

}
