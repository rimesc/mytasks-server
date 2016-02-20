package uk.co.zoneofavoidance.my.tasks.controllers;

import java.beans.PropertyEditorSupport;
import java.util.Locale;
import java.util.Optional;

class EnumConverter<T extends Enum<T>> extends PropertyEditorSupport {

   private final Class<T> enumClass;
   private T onErrorValue;

   public EnumConverter(final Class<T> enumClass) {
      this.enumClass = enumClass;
   }

   public EnumConverter(final Class<T> enumClass, final T onErrorValue) {
      this.enumClass = enumClass;
      this.onErrorValue = onErrorValue;
   }

   @Override
   public void setAsText(final String text) throws IllegalArgumentException {
      setValue(parseValue(text));
   }

   private T parseValue(final String text) {
      try {
         return Enum.valueOf(enumClass, text.toUpperCase(Locale.US));
      }
      catch (final IllegalArgumentException ex) {
         return Optional.ofNullable(onErrorValue).orElseThrow(() -> ex);
      }
   }

}
