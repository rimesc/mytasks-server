package uk.co.zoneofavoidance.my.tasks.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * REST response containing a 'read-me' in both raw markdown and HTML formats.
 */
@JsonPropertyOrder({ "html", "markdown" })
public class NotesJson {

   static final NotesJson EMPTY = new NotesJson("", "");

   private final String html;

   private final String raw;

   /**
    * @param html formatted HTML
    * @param raw raw markdown
    */
   public NotesJson(final String html, final String raw) {
      this.html = html;
      this.raw = raw;
   }

   /**
    * @return the formatted HTML version of the 'read-me'
    */
   public String getHtml() {
      return html;
   }

   /**
    * @return the raw markdown version of the 'read-me'
    */
   public String getRaw() {
      return raw;
   }

}
