package uk.co.zoneofavoidance.my.tasks.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Utilities for working with enumerations.
 */
public final class EnumUtils {

   /**
    * Returns a user-friendly display name for an enum constant, based on the
    * constant name.
    *
    * @param name name of the enum constant
    * @return user-friendly display name
    */
   public static String toString(final String name) {
      return StringUtils.capitalize(name.toLowerCase().replace('_', ' '));
   }

   private EnumUtils() {
   }
}
