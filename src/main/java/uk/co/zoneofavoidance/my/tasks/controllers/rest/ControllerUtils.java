package uk.co.zoneofavoidance.my.tasks.controllers.rest;

import java.util.function.Consumer;

public class ControllerUtils {

   static <T> void setIfNotNull(final T valueOrNull, final Consumer<T> setter) {
      if (valueOrNull != null) {
         setter.accept(valueOrNull);
      }
   }

}
