package uk.co.zoneofavoidance.my.tasks.controllers;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.exparity.hamcrest.date.DateMatchers;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * A collection of wrappers for standard Hamcrest matchers to adapt them to
 * match JSON strings.
 */
public final class JsonStringMatchers {

   /**
    * Matcher that matches when the examined date is within a defined period of
    * the reference date.
    *
    * @param period period within which the examined date must be
    * @param unit unit in which period is specified
    * @param date reference date
    * @return the matcher
    * @see DateMatchers#within(long, java.util.concurrent.TimeUnit, Date)
    */
   public static Matcher<String> dateWithin(final long period, final ChronoUnit unit, final Date date) {
      return new TransformingMatcher<>(item -> new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ").parse(item), DateMatchers.within(period, unit, date));
   }

   private static class TransformingMatcher<T> extends BaseMatcher<String> {

      private final Mapper<T> mapper;
      private final Matcher<T> delegate;

      TransformingMatcher(final Mapper<T> mapper, final Matcher<T> delegate) {
         this.mapper = mapper;
         this.delegate = delegate;
      }

      @Override
      public boolean matches(final Object item) {
         try {
            return item instanceof String && delegate.matches(mapper.apply((String) item));
         }
         catch (final Exception e) {
            return false;
         }
      }

      @Override
      public void describeTo(final Description description) {
         delegate.describeTo(description);
      }

   }

   @FunctionalInterface
   private interface Mapper<T> {
      T apply(String item) throws Exception;
   }

   private JsonStringMatchers() {
   }

}
