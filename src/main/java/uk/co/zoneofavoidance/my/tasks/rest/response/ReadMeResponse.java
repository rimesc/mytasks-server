package uk.co.zoneofavoidance.my.tasks.rest.response;

public class ReadMeResponse {

   private final String html;

   private final String markdown;

   public ReadMeResponse(final String html, final String markdown) {
      this.html = html;
      this.markdown = markdown;
   }

   public String getHtml() {
      return html;
   }

   public String getMarkdown() {
      return markdown;
   }

}
