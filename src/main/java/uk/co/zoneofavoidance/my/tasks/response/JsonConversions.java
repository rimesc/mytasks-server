package uk.co.zoneofavoidance.my.tasks.response;

import static uk.co.zoneofavoidance.my.tasks.response.NotesJson.EMPTY;

import org.pegdown.PegDownProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.co.zoneofavoidance.my.tasks.domain.Note;
import uk.co.zoneofavoidance.my.tasks.domain.Project;

/**
 * Conversions from domain objects to JSON responses.
 */
@Component
public class JsonConversions {

   private final PegDownProcessor markdown;

   @Autowired
   public JsonConversions(final PegDownProcessor markdown) {
      this.markdown = markdown;
   }

   /**
    * @param project domain object
    * @return basic project JSON, containing just the name and unique identifier
    */
   public BasicProjectJson toBasicJson(final Project project) {
      return new BasicProjectJson(project.getId(), project.getName());
   }

   /**
    * @param project domain object
    * @return abridged project JSON, without notes
    */
   public ProjectJson toAbridgedJson(final Project project) {
      return toJson(project, null);
   }

   /**
    * @param project domain object
    * @return complete project JSON
    */
   public ProjectJson toJson(final Project project) {
      return toJson(project, json(project.getReadMe()));
   }

   /**
    * @param notes domain object
    * @return notes JSON
    */
   public NotesJson json(final Note notes) {
      return notes == null ? EMPTY : new NotesJson(markdown.markdownToHtml(notes.getText()), notes.getText());
   }

   private ProjectJson toJson(final Project project, final NotesJson notes) {
      return new ProjectJson(toBasicJson(project), project.getDescription(), project.getNumberOfTasks(), project.getNumberOfOpenTasks(), notes);
   }

}
