package uk.co.zoneofavoidance.my.tasks.request;

import static org.hamcrest.Matchers.contains;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;

import uk.co.zoneofavoidance.my.tasks.domain.Priority;
import uk.co.zoneofavoidance.my.tasks.domain.State;
import uk.co.zoneofavoidance.my.tasks.request.validation.Create;

/**
 * Unit tests for {@link TaskForm}.
 */
public class TaskFormTest extends FormValidationTestBase<TaskForm> {

   private static final int MAX_SUMMARY_LENGTH = 255;

   private TaskForm form;

   @Before
   public void setUpForm() throws Exception {
      form = new TaskForm();
      form.setSummary("My task");
      form.setDescription("My task.");
      form.setPriority(Priority.NORMAL);
      form.setTags(ImmutableSet.of("foo", "bar"));
   }

   @Test
   public void completeFormIsOKOnCreation() throws Exception {
      assertValid(Create.class);
   }

   @Test
   public void missingTagsAreOKOnCreation() throws Exception {
      form.setTags(null);
      assertValid(Create.class);
   }

   @Test
   public void missingSummaryIsAnErrorOnCreation() {
      form.setSummary(null);
      assertConstraintViolations(contains(constraintViolation("summary", "may not be null")), Create.class);
   }

   @Test
   public void missingDescriptionIsAnErrorOnCreation() {
      form.setDescription(null);
      assertConstraintViolations(contains(constraintViolation("description", "may not be null")), Create.class);
   }

   @Test
   public void missingPriorityIsAnErrorOnCreation() {
      form.setPriority(null);
      assertConstraintViolations(contains(constraintViolation("priority", "may not be null")), Create.class);
   }

   @Test
   public void missingFieldsAreOKOnUpdate() {
      form = new TaskForm();
      assertValid();
   }

   @Test
   public void emptySummaryIsAnError() {
      form.setSummary("");
      assertConstraintViolations(contains(constraintViolation("summary", "length must be between 1 and 255")));
   }

   @Test
   public void longSummaryIsAnError() {
      form.setSummary(Strings.repeat("$", MAX_SUMMARY_LENGTH + 1));
      assertConstraintViolations(contains(constraintViolation("summary", "length must be between 1 and 255")));
   }

   @Test
   public void stateIsNotAllowedOnCreation() throws Exception {
      form.setState(State.DONE);
      assertConstraintViolations(contains(constraintViolation("state", "must be null")), Create.class);
   }

   @Override
   protected TaskForm form() {
      return form;
   }

}
