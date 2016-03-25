package uk.co.zoneofavoidance.my.tasks.testing;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.net.URI;

import org.hamcrest.Matcher;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertThat;

import uk.co.zoneofavoidance.my.tasks.controllers.MyTasksControllerIT;
import uk.co.zoneofavoidance.my.tasks.domain.Project;
import uk.co.zoneofavoidance.my.tasks.domain.Task;

/**
 * Integration test using {@link MockMvc}.
 */
public abstract class ModelAndViewControllerIT extends BaseMockMvcTest {

   private static final String REDIRECT_PREFIX = "redirect:";

   /**
    * Perform an HTTP GET request to the given path.
    *
    * @param path the path within the servlet context
    * @return an instance of {@link ResultActions}; never {@code null}
    * @throws Exception if something went wrong
    */
   protected ResultActions get(final String path) throws Exception {
      return mockMvc().perform(MockMvcRequestBuilders.get(new URI(path)).with(csrf()));
   }

   /**
    * Perform an HTTP POST request to the given path.
    *
    * @param path the path within the servlet context
    * @return an instance of {@link ResultActions}; never {@code null}
    * @throws Exception if something went wrong
    */
   protected ResultActions post(final String path) throws Exception {
      return mockMvc().perform(MockMvcRequestBuilders.post(new URI(path)));
   }

   /**
    * Matcher that matches an iterable sequence of projects if they have the
    * given names in the given order.
    *
    * @param names ordered sequence of names to match
    * @return a matcher
    */
   protected static Matcher<Iterable<? extends Project>> containsProjects(final String... names) {
      return contains(stream(names).map(MyTasksControllerIT::named).collect(toList()));
   }

   /**
    * Matcher that matches an iterable sequence of tasks if they have the given
    * IDs in the given order.
    *
    * @param names ordered sequence of IDs to match
    * @return a matcher
    */
   protected static Matcher<Iterable<? extends Task>> containsTasks(final Long... ids) {
      return contains(stream(ids).map(id -> hasProperty("id", equalTo(id))).collect(toList()));
   }

   /**
    * Matcher that matches any object that has a property names 'name' that has
    * the given value.
    *
    * @param name the name to match
    * @return a matcher
    */
   protected static Matcher<Object> named(final String name) {
      return hasProperty("name", equalTo(name));
   }

   /**
    * Extracts the path from a view name of the form "redirect:{@code <path>}".
    *
    * @param view the view name
    * @return the path
    */
   protected static String toRedirectPath(final String view) {
      assertThat(view, isRedirect());
      return view.substring(REDIRECT_PREFIX.length());
   }

   /**
    * Matcher that matches a view name of the form "redirect:{@code <path>}".
    *
    * @return a matcher
    */
   protected static Matcher<String> isRedirect(final String path) {
      return startsWith(REDIRECT_PREFIX);
   }

   /**
    * Matcher that matches view names that begin with the string "redirect:".
    *
    * @return a matcher
    */
   protected static Matcher<String> isRedirect() {
      return startsWith(REDIRECT_PREFIX);
   }

}
