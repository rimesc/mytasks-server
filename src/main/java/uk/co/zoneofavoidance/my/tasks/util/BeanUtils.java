package uk.co.zoneofavoidance.my.tasks.util;

import java.util.function.Consumer;

public class BeanUtils {

   public static <T> void setIfNotNull(final T valueOrNull, final Consumer<T> setter) {
      if (valueOrNull != null) {
         setter.accept(valueOrNull);
      }
   }

}
