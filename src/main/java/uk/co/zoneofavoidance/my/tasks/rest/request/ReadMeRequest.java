package uk.co.zoneofavoidance.my.tasks.rest.request;

/**
 * Request body for updating a read-me.
 */
public class ReadMeRequest {

   private String markdown;

   /**
    * @return the raw markdown version of the 'read-me'
    */
   public String getMarkdown() {
      return markdown;
   }

   /**
    * @param markdown raw markdown
    */
   public void setMarkdown(final String markdown) {
      this.markdown = markdown;
   }

}
