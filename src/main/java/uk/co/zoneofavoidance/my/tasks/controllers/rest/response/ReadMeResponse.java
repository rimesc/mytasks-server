package uk.co.zoneofavoidance.my.tasks.controllers.rest.response;

public class ReadMeResponse {

   private final long project;

   private final String html;

   private final String markdown;

   public ReadMeResponse(final long project, final String html, final String markdown) {
      this.project = project;
      this.html = html;
      this.markdown = markdown;
   }

   public Long getProject() {
      return project;
   }

   public String getHtml() {
      return html;
   }

   public String getMarkdown() {
      return markdown;
   }

}
