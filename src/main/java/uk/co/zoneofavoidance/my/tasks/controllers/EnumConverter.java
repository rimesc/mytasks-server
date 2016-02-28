package uk.co.zoneofavoidance.my.tasks.controllers;

import java.beans.PropertyEditorSupport;
import java.util.Locale;
import java.util.Optional;

import com.google.common.base.Preconditions;

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

   @Override
   public String getAsText() {
      return getValue().name().toLowerCase(Locale.US);
   }

   @Override
   public void setValue(final Object value) {
      Preconditions.checkArgument(enumClass.isInstance(value), "Value must be an instance of " + enumClass.getSimpleName());
      super.setValue(value);
   }

   @Override
   @SuppressWarnings("unchecked")
   public T getValue() {
      return (T) super.getValue();
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
