package uk.co.zoneofavoidance.my.tasks.repositories;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.util.List;
import java.util.function.Function;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertThat;

import uk.co.zoneofavoidance.my.tasks.MyTasksApplication;
import uk.co.zoneofavoidance.my.tasks.domain.Tag;

@IntegrationTest
@SpringApplicationConfiguration
@ActiveProfiles("dev")
@RunWith(SpringJUnit4ClassRunner.class)
public class TagRepositoryIT {

   @Autowired
   private TagRepository tags;

   private Tag unusedTag;

   @Before
   public void setUp() {
      unusedTag = tags.save(Tag.create("Unused"));
   }

   @After
   public void tearDown() {
      tags.delete(unusedTag);
   }

   @Test
   public void findAllIncludesUnused() {
      assertThat(getTagNames(TagRepository::findAll), containsInAnyOrder("Bug", "Feature", "Unused", "Version 1"));
   }

   @Test
   public void findUsedExcludesUnused() {
      assertThat(getTagNames(TagRepository::findUsed), containsInAnyOrder("Bug", "Feature", "Version 1"));
   }

   private List<String> getTagNames(final Function<TagRepository, List<Tag>> accessor) {
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
