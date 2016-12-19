package uk.co.zoneofavoidance.my.tasks.util;

import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toSet;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

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

   /**
    * Maps and sets a set-typed bean property only if the source value is
    * non-<code>null</code>.
    *
    * @param <S> type of the source values
    * @param <T> type of the target values
    * @param valueOrNull the value to set, or <code>null</code>
    * @param setter reference to the property setter method
    */
   public static <S, T> void setIfNotNull(final Set<S> valueOrNull, final Function<S, T> mapper, final Consumer<Set<T>> setter) {
      if (valueOrNull != null) {
         setter.accept(valueOrNull.stream().map(mapper).collect(toSet()));
      }
   }

   /**
    * Sets a set-typed bean property only if it is non-<code>null</code>.
    *
    * @param <T> type of the values
    * @param valueOrNull the value to set, or <code>null</code>
    * @param setter reference to the property setter method
    */
   public static <T> void setIfNotNull(final Set<T> valueOrNull, final Consumer<Set<T>> setter) {
      setIfNotNull(valueOrNull, identity(), setter);
   }

}
