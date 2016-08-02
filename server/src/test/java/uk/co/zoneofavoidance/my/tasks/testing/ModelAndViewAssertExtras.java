package uk.co.zoneofavoidance.my.tasks.testing;

import static org.springframework.test.util.AssertionErrors.assertTrue;

import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Additions to {@link ModelAndViewAssert}.
 */
public final class ModelAndViewAssertExtras {

   /**
    * Asserts that the given {@code ModelAndView} is a redirect to the given
    * path (i.e. the view name is 'redirect:{@code path}'.
    *
    * @param mav {@code ModelAndView} to test against
    * @param expectedPath expected path
    */
   public static void assertRedirect(final ModelAndView mav, final String expectedPath) {
      assertTrue("ModelAndView is null", mav != null);
      assertEquals("redirect:" + expectedPath, mav.getViewName());
   }

   /**
    * Assert that a model attribute is <strong>not</strong> available.
    *
    * @param mav ModelAndView to test against (never {@code null})
    * @param modelName name of the object to add to the model (never
    *           {@code null})
    * @see ModelAndViewAssert#assertModelAttributeAvailable(ModelAndView,
    *      String)
    */
   public static void assertModelAttributeNotAvailable(final ModelAndView mav, final String modelName) {
      assertTrue("ModelAndView is null", mav != null);
      assertTrue("Model is null", mav.getModel() != null);
      assertFalse("Model attribute with name '" + modelName + "' is available", mav.getModel().containsKey(modelName));
   }

}
