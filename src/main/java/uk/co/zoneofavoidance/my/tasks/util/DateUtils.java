package uk.co.zoneofavoidance.my.tasks.util;

import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public final class DateUtils {

   static final String THYMELEAF_FORMAT_DATE = "thymeleaf.format.date";
   static final String THYMELEAF_FORMAT_TIME = "thymeleaf.format.time";

   private final SimpleDateFormat _dateFormat;
   private final SimpleDateFormat _timeFormat;

   @Autowired
   public DateUtils(final MessageSource messages) {
      _dateFormat = new SimpleDateFormat(messages.getMessage(THYMELEAF_FORMAT_DATE, new Object[0], "yyyy-MM-dd", Locale.getDefault()));
      _timeFormat = new SimpleDateFormat(messages.getMessage(THYMELEAF_FORMAT_TIME, new Object[0], "HH:mm:ss", Locale.getDefault()));
   }

   public String formatDateTime(final Date date) {
      if (isToday(date)) {
         return _timeFormat.format(date);
      }
      return _dateFormat.format(date);
   }

   public boolean isToday(final Date date) {
      return LocalDate.now().equals(toLocalDate(date));
   }

   private LocalDate toLocalDate(final Date date) {
      final Calendar cal = GregorianCalendar.getInstance();
      cal.setTime(date);
      final LocalDate localDate = LocalDate.of(cal.get(YEAR), cal.get(MONTH) + 1, cal.get(Calendar.DATE));
      return localDate;
   }

}
