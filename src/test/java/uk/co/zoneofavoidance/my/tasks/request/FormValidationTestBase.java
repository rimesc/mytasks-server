package uk.co.zoneofavoidance.my.tasks.request;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasToString;

import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.hamcrest.Matcher;
import org.junit.Before;

import com.google.common.collect.Lists;

import static org.junit.Assert.assertThat;

/**
 * Base class for unit testing form validation.
 *
 * @param <T> type of form under test
 */
public abstract class FormValidationTestBase<T> {

   private Validator validator;

   @Before
   public final void setUpValidator() throws Exception {
      validator = Validation.buildDefaultValidatorFactory().getValidator();
   }

   /**
    * @return the form to be validated
    */
   protected abstract T form();

   /**
    * Assert that there are no constraint violations for the given validation
    * group(s).
    *
    * @param groups validation groups ({@link Default} is added automatically)
    */
   protected final void assertValid(final Class<?>... groups) {
      assertConstraintViolations(emptyIterable(), groups);
   }

   /**
    * Assert the constraint violations for the given validation group(s).
    *
    * @param matcher matcher for the constraint violations
    * @param groups validation groups ({@link Default} is added automatically)
    */
   protected final void assertConstraintViolations(final Matcher<Iterable<? extends ConstraintViolation<T>>> matcher, final Class<?>... groups) {
      assertThat(validator.validate(form(), appendDefaultGroup(groups)), matcher);
   }

   private Class<?>[] appendDefaultGroup(final Class<?>... groups) {
      final List<Class<?>> allGroups = Lists.newArrayList(groups);
      allGroups.add(Default.class);
      return allGroups.toArray(new Class<?>[0]);
   }

   /**
    * Matches a constraint violation for the given property with the given
    * message.
    *
    * @param property expected property
    * @param message expected message
    * @return a matcher
    */
   //
   protected final Matcher<ConstraintViolation<T>> constraintViolation(final String property, final String message) {
      return allOf(hasProperty("propertyPath", hasToString(property)), hasProperty("message", equalTo(message)));
   }

}
