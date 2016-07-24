package uk.co.zoneofavoidance.my.tasks.repositories;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertThat;

import uk.co.zoneofavoidance.my.tasks.MyTasksApplication;
import uk.co.zoneofavoidance.my.tasks.domain.Tag;

/**
 * Integration tests for {@link TagRepository}.
 */
@IntegrationTest
@SpringApplicationConfiguration
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "/sql/setup_TagRepositoryIT.sql", executionPhase = BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/cleanup.sql", executionPhase = AFTER_TEST_METHOD)
public class TagRepositoryIT {

   @Autowired
   private TagRepository tags;

   @Test
   public void findAllIncludesUnused() {
      assertThat(getTagNames(TagRepository::findAll), containsInAnyOrder("First", "Second", "Third", "Fourth", "Unused"));
   }

   @Test
   public void findUsedExcludesUnused() {
      assertThat(getTagNames(TagRepository::findUsed), containsInAnyOrder("First", "Second", "Third", "Fourth"));
   }

   private List<String> getTagNames(final Function<TagRepository, Collection<Tag>> accessor) {
      return accessor.apply(tags).stream().map(Tag::getName).collect(toList());
   }

   /**
    * Hook from which to hang an import of the main configuration.
    */
   @Configuration
   @Import(MyTasksApplication.class)
   public static class Config {
   }

}
