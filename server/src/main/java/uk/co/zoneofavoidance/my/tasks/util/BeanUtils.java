package uk.co.zoneofavoidance.my.tasks.util;

import java.util.function.Consumer;

/**
 * Utility methods for working with bean properties.
 */
public class BeanUtils {

   /**
    * Sets a bean property only if it is non-<code>null</code>.
    *
    * @param <T> type of the value
    * @param valueOrNull the value to set, or <code>null</code>
    * @param setter reference to the property setter method
    */
   public static <T> void setIfNotNull(final T valueOrNull, final Consumer<T> setter) {
      if (valueOrNull != null) {
         setter.accept(valueOrNull);
      }
   }

}
