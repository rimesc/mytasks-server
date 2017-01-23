package uk.co.zoneofavoidance.my.tasks.services;

import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.google.common.collect.ImmutableSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import uk.co.zoneofavoidance.my.tasks.domain.Tag;
import uk.co.zoneofavoidance.my.tasks.repositories.TagRepository;

/**
 * Unit tests for {@link TaskService}.
 */
public class TagServiceTest {

   private static final Tag FOO_TAG = Tag.create("foo");
   private static final Tag BAR_TAG = Tag.create("bar");
   private static final Tag BAZ_TAG = Tag.create("baz");

   @Mock
   private TagRepository repository;

   private TagService service;

   @Before
   public void setUp() throws Exception {
      service = new TagService(repository);
   }

   @Test
   public void getReturnsExistingTag() {
      when(repository.findByName("foo")).thenReturn(FOO_TAG);
      assertEquals(FOO_TAG, service.get("foo"));
   }

   @Test
   public void getReturnsNewTag() {
      when(repository.findByName("bar")).thenReturn(null);
      assertEquals("bar", service.get("bar").getName());
   }

   @Test
   public void getAllReturnsUsedTagsOrdered() {
      when(repository.findUsed()).thenReturn(ImmutableSet.of(FOO_TAG, BAR_TAG, BAZ_TAG));
      assertThat(service.getAll(), contains(BAR_TAG, BAZ_TAG, FOO_TAG));
   }

   @Rule
   public MockitoRule mockitoRule() {
      return MockitoJUnit.rule();
   }

}
