package uk.co.zoneofavoidance.my.tasks.util;

import static uk.co.zoneofavoidance.my.tasks.util.DateUtils.THYMELEAF_FORMAT_DATE;
import static uk.co.zoneofavoidance.my.tasks.util.DateUtils.THYMELEAF_FORMAT_TIME;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.StaticMessageSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link DateUtils}.
 */
public class DateUtilsTest {

   private StaticMessageSource messages;
   private DateUtils dates;

   @Before
   public void setUp() throws Exception {
      messages = new StaticMessageSource();
   }

   @Test
   public void testFormatDateToday() {
      // default format: HH:mm:ss
      dates = new DateUtils(messages);
      testFormatDateTime("08:09:04", LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 9, 4)));
      testFormatDateTime("11:00:31", LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 0, 31)));
      testFormatDateTime("22:14:00", LocalDateTime.of(LocalDate.now(), LocalTime.of(22, 14, 0)));
   }

   @Test
   public void testFormatDateNotToday() {
      // default format: yyyy-MM-dd
      dates = new DateUtils(messages);
      testFormatDateTime("2015-04-07", LocalDateTime.of(2015, 4, 7, 8, 9, 4));
      testFormatDateTime("2001-11-01", LocalDateTime.of(2001, 11, 1, 11, 0, 31));
      testFormatDateTime("1999-12-31", LocalDateTime.of(1999, 12, 31, 22, 14, 0));
   }

   @Test
   public void testFormatDateTodayCustomFormat() {
      messages.addMessage(THYMELEAF_FORMAT_TIME, Locale.getDefault(), "h:mm a");
      dates = new DateUtils(messages);
      testFormatDateTime("8:09 AM", LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 9, 4)));
      testFormatDateTime("11:00 AM", LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 0, 31)));
      testFormatDateTime("10:14 PM", LocalDateTime.of(LocalDate.now(), LocalTime.of(22, 14, 0)));
   }

   @Test
   public void testFormatDateNotTodayCustomFormat() {
      messages.addMessage(THYMELEAF_FORMAT_DATE, Locale.getDefault(), "d MMMM yyyy");
      dates = new DateUtils(messages);
      testFormatDateTime("7 April 2015", LocalDateTime.of(2015, 4, 7, 8, 9, 4));
      testFormatDateTime("1 November 2001", LocalDateTime.of(2001, 11, 1, 11, 0, 31));
      testFormatDateTime("31 December 1999", LocalDateTime.of(1999, 12, 31, 22, 14, 0));
   }

   @Test
   public void testIsToday() {
      dates = new DateUtils(messages);
      assertTrue(dates.isToday(asDate(LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 9, 4)))));
      assertTrue(dates.isToday(asDate(LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 0, 31)))));
      assertTrue(dates.isToday(asDate(LocalDateTime.of(LocalDate.now(), LocalTime.of(22, 14, 0)))));
      assertFalse(dates.isToday(asDate(LocalDateTime.of(2015, 4, 7, 8, 9, 4))));
      assertFalse(dates.isToday(asDate(LocalDateTime.of(2001, 11, 1, 11, 0, 31))));
      assertFalse(dates.isToday(asDate(LocalDateTime.of(1999, 12, 31, 22, 14, 0))));
   }

   private void testFormatDateTime(final String expected, final LocalDateTime dateTime) {
      assertEquals(expected, dates.formatDateTime(asDate(dateTime)));
   }

   private Date asDate(final LocalDateTime localDateTime) {
      return new Date(localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli());
   }

}
