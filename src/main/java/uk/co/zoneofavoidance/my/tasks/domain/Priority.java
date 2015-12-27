package uk.co.zoneofavoidance.my.tasks.domain;

import uk.co.zoneofavoidance.my.tasks.util.EnumUtils;

public enum Priority {

   LOW, NORMAL, HIGH, CRITICAL;

   @Override
   public String toString() {
      return EnumUtils.toString(name());
   };

}
