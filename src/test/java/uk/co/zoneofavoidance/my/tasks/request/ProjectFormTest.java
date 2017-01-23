package uk.co.zoneofavoidance.my.tasks.request;

import static org.hamcrest.Matchers.contains;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Strings;

import uk.co.zoneofavoidance.my.tasks.request.validation.Create;

/**
 * Unit tests for {@link ProjectForm}.
 */
public class ProjectFormTest extends FormValidationTestBase<ProjectForm> {

   private static final int MAX_SUMMARY_LENGTH = 255;

   private ProjectForm form;

   @Before
   public void setUpForm() throws Exception {
      form = new ProjectForm();
      form.setName("My project");
      form.setDescription("My project.");
   }

   @Test
   public void completeFormIsOKOnCreation() throws Exception {
      assertValid(Create.class);
   }

   @Test
   public void missingNameIsAnErrorOnCreation() {
      form.setName(null);
      assertConstraintViolations(contains(constraintViolation("name", "may not be null")), Create.class);
   }

   @Test
   public void missingFieldsAreOKOnUpdate() {
      form = new ProjectForm();
      assertValid();
   }

   @Test
   public void emptyNameIsAnError() {
      form.setName("");
      assertConstraintViolations(contains(constraintViolation("name", "length must be between 1 and 255")));
   }

   @Test
   public void longNameIsAnError() {
      form.setName(Strings.repeat("$", MAX_SUMMARY_LENGTH + 1));
      assertConstraintViolations(contains(constraintViolation("name", "length must be between 1 and 255")));
   }

   @Override
   protected ProjectForm form() {
      return form;
   }

}
