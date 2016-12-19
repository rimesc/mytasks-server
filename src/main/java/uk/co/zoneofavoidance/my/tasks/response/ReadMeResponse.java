package uk.co.zoneofavoidance.my.tasks.response;

/**
 * REST response containing a 'read-me' in both raw markdown and HTML formats.
 */
public class ReadMeResponse {

   private final String html;

   private final String markdown;

   /**
    * @param html formatted HTML
    * @param markdown raw markdown
    */
   public ReadMeResponse(final String html, final String markdown) {
      this.html = html;
      this.markdown = markdown;
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
   public String getMarkdown() {
      return markdown;
   }

}
