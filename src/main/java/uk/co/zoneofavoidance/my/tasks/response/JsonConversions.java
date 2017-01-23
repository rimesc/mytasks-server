package uk.co.zoneofavoidance.my.tasks.response;

import static java.util.stream.Collectors.toList;
import static uk.co.zoneofavoidance.my.tasks.response.NotesJson.EMPTY;

import org.pegdown.PegDownProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.co.zoneofavoidance.my.tasks.domain.Note;
import uk.co.zoneofavoidance.my.tasks.domain.Project;
import uk.co.zoneofavoidance.my.tasks.domain.Tag;
import uk.co.zoneofavoidance.my.tasks.domain.Task;

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
    * @return complete task JSON
    */
   public ProjectJson toJson(final Project project) {
      return toJson(project, json(project.getReadMe()));
   }

   /**
    * @param task domain object
    * @return abridged task JSON, without notes
    */
   public TaskJson toAbridgedJson(final Task task) {
      return toJson(task, null);
   }

   /**
    * @param task domain object
    * @return complete project JSON
    */
   public TaskJson toJson(final Task task) {
      return toJson(task, json(task.getDescription()));
   }

   /**
    * @param notes domain object
    * @return notes JSON
    */
   public NotesJson json(final Note notes) {
      return notes == null ? EMPTY : new NotesJson(markdown.markdownToHtml(notes.getText()), notes.getText());
   }

   /**
    * @param notes text
    * @return notes JSON
    */
   public NotesJson json(final String notes) {
      return notes == null ? EMPTY : new NotesJson(markdown.markdownToHtml(notes), notes);
   }

   private ProjectJson toJson(final Project project, final NotesJson notes) {
      return new ProjectJson(toBasicJson(project), project.getDescription(), project.getNumberOfTasks(), project.getNumberOfOpenTasks(), notes);
   }

   private TaskJson toJson(final Task task, final NotesJson notes) {
      return new TaskJson(task.getId(), task.getSummary(), task.getPriority(), task.getState(), task.getTags().stream().map(Tag::getName).sorted().collect(toList()), task.getCreated(), task.getUpdated(), notes, toBasicJson(task.getProject()));
   }

}
