package uk.co.zoneofavoidance.my.tasks.controllers;

import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.ModelAndViewAssert.assertAndReturnModelAttributeOfType;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeValue;
import static org.springframework.test.web.ModelAndViewAssert.assertViewName;
import static uk.co.zoneofavoidance.my.tasks.domain.Priority.HIGH;
import static uk.co.zoneofavoidance.my.tasks.domain.Priority.NORMAL;
import static uk.co.zoneofavoidance.my.tasks.domain.State.DONE;
import static uk.co.zoneofavoidance.my.tasks.domain.State.TO_DO;
import static uk.co.zoneofavoidance.my.tasks.testing.ModelAndViewAssertExtras.assertRedirect;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import uk.co.zoneofavoidance.my.tasks.domain.Priority;
import uk.co.zoneofavoidance.my.tasks.domain.Project;
import uk.co.zoneofavoidance.my.tasks.domain.Task;
import uk.co.zoneofavoidance.my.tasks.exceptions.NotFoundException;
import uk.co.zoneofavoidance.my.tasks.repositories.ProjectRepository;
import uk.co.zoneofavoidance.my.tasks.services.TaskService;

/**
 * Unit tests for {@link TasksController}.
 * <p>
 * Note that while these tests check the returned {@link ModelAndView} and the
 * thrown exceptions, they lack parameter validation and exception handling.
 */
public class TasksControllerTest {

   private static final long PROJECT_ID = 157L;
   private static final long TASK_ID = 324L;

   private static final Project FOO_PROJECT = new Project("Foo", "The Foo project.");
   private static final Task FOO_TASK = new Task(FOO_PROJECT, "Foo", "The Foo task.", NORMAL, TO_DO);

   @Mock
   private TaskService taskService;

   @Mock
   private ProjectRepository repository;

   @Mock
   private BindingResult bindingResult;

   private TasksController controller;

   @Before
   public void setUp() throws Exception {
      controller = new TasksController(repository, taskService);
   }

   @Test
   public void getTaskReturnsTaskViewWithTask() {
      when(taskService.get(TASK_ID)).thenReturn(FOO_TASK);
      final ModelAndView modelAndView = controller.getTask(TASK_ID);
      assertViewName(modelAndView, "tasks/view");
      assertModelAttributeValue(modelAndView, "task", FOO_TASK);
   }

   @Test(expected = NotFoundException.class)
   public void getTaskPropagatesException() {
      when(taskService.get(TASK_ID)).thenThrow(new NotFoundException("task"));
      controller.getTask(TASK_ID);
   }

   @Test
   public void postTaskUpdatesTaskStateAndReturnsTaskViewWithTask() {
      when(taskService.get(TASK_ID)).thenReturn(new Task(FOO_PROJECT, "Open", "An open task.", NORMAL, TO_DO));
      final ModelAndView modelAndView = controller.postTask(TASK_ID, DONE);
      assertViewName(modelAndView, "tasks/view");
      final Task task = assertAndReturnModelAttributeOfType(modelAndView, "task", Task.class);
      assertEquals(DONE, task.getState());
      final ArgumentCaptor<Task> argument = ArgumentCaptor.forClass(Task.class);
      verify(taskService).save(argument.capture());
      assertEquals(DONE, argument.getValue().getState());
   }

   @Test(expected = NotFoundException.class)
   public void postTaskPropagatesException() {
      when(taskService.get(TASK_ID)).thenThrow(new NotFoundException("task"));
      controller.postTask(TASK_ID, DONE);
   }

   @Test
   public void getNewTaskReturnsNewTaskViewWithPriorities() {
      when(repository.findOne(PROJECT_ID)).thenReturn(FOO_PROJECT);
      final ModelAndView modelAndView = controller.getNewTask(new TaskForm(PROJECT_ID));
      assertViewName(modelAndView, "tasks/new");
      final Priority[] priorities = assertAndReturnModelAttributeOfType(modelAndView, "priorities", Priority[].class);
      assertThat(priorities, arrayContainingInAnyOrder(Priority.values()));
   }

   @Test(expected = NotFoundException.class)
   public void getNewTaskPropagatesException() {
      when(repository.findOne(PROJECT_ID)).thenReturn(FOO_PROJECT);
      when(taskService.get(TASK_ID)).thenThrow(new NotFoundException("task"));
      controller.getNewTask(new TaskForm());
   }

   @Test(expected = NotFoundException.class)
   public void getNewTaskThrowsIfProjectNotFound() {
      when(repository.findOne(PROJECT_ID)).thenReturn(null);
      controller.getNewTask(new TaskForm(PROJECT_ID));
   }

   @Test
   public void postNewTaskCreatesTaskAndRedirectsToTaskView() {
      when(bindingResult.hasErrors()).thenReturn(false);
      when(repository.findOne(PROJECT_ID)).thenReturn(FOO_PROJECT);
      when(taskService.save(isA(Task.class))).thenAnswer(invocation -> {
         final Task task = invocation.getArgumentAt(0, Task.class);
         task.setId(TASK_ID);
         return task;
      });
      final TaskForm form = new TaskForm(PROJECT_ID, "New task", "A new task.", NORMAL);
      final ModelAndView modelAndView = controller.postNewTask(form, bindingResult);
      assertRedirect(modelAndView, "/tasks/" + TASK_ID);
      verify(taskService).save(isA(Task.class));
   }

   @Test
   public void postNewTaskReturnsNewTaskViewIfBindingErrors() {
      when(bindingResult.hasErrors()).thenReturn(true);
      when(repository.findOne(PROJECT_ID)).thenReturn(FOO_PROJECT);
      final TaskForm form = new TaskForm(PROJECT_ID, "New task", "A new task.", NORMAL);
      final ModelAndView modelAndView = controller.postNewTask(form, bindingResult);
      assertViewName(modelAndView, "tasks/new");
      assertModelAttributeValue(modelAndView, "taskForm", form);
   }

   @Test(expected = NotFoundException.class)
   public void postNewTaskThrowsIfProjectNotFound() {
      when(repository.findOne(PROJECT_ID)).thenReturn(null);
      controller.postNewTask(new TaskForm(PROJECT_ID), bindingResult);
   }

   @Test(expected = NotFoundException.class)
   public void postNewTaskPropagatesException() {
      when(taskService.get(TASK_ID)).thenThrow(new NotFoundException("task"));
      controller.postNewTask(new TaskForm(), bindingResult);
   }

   @Test
   public void getEditTaskReturnsEditTaskViewWithPriorities() {
      when(taskService.get(TASK_ID)).thenReturn(FOO_TASK);
      final ModelAndView modelAndView = controller.getEditTask(TASK_ID);
      assertViewName(modelAndView, "tasks/edit");
      final TaskForm form = assertAndReturnModelAttributeOfType(modelAndView, "taskForm", TaskForm.class);
      assertEquals("Foo", form.getSummary());
      assertEquals("The Foo task.", form.getDescription());
      final Priority[] priorities = assertAndReturnModelAttributeOfType(modelAndView, "priorities", Priority[].class);
      assertThat(priorities, arrayContainingInAnyOrder(Priority.values()));
   }

   @Test(expected = NotFoundException.class)
   public void getEditTaskPropagatesException() {
      when(taskService.get(TASK_ID)).thenThrow(new NotFoundException("task"));
      controller.getEditTask(TASK_ID);
   }

   @Test
   public void postEditTaskUpdatesTaskAndRedirectsToTaskView() throws Exception {
      when(taskService.get(TASK_ID)).thenReturn(new Task(FOO_PROJECT, "My Task", "Old description.", NORMAL));
      when(bindingResult.hasErrors()).thenReturn(false);
      final TaskForm form = new TaskForm(PROJECT_ID, "My Task", "New description.", HIGH);
      final ModelAndView modelAndView = controller.postEditTask(TASK_ID, form, bindingResult);
      assertRedirect(modelAndView, "/tasks/" + TASK_ID);
      final ArgumentCaptor<Task> argument = ArgumentCaptor.forClass(Task.class);
      verify(taskService).save(argument.capture());
      assertEquals("New description.", argument.getValue().getDescription());
      assertEquals(HIGH, argument.getValue().getPriority());
   }

   @Test
   public void postEditTaskReturnsEditTaskViewIfBindingErrors() throws Exception {
      when(taskService.get(TASK_ID)).thenReturn(FOO_TASK);
      when(bindingResult.hasErrors()).thenReturn(true);
      final TaskForm form = new TaskForm(PROJECT_ID, "Illegal Name", "The Foo task.", NORMAL);
      final ModelAndView modelAndView = controller.postEditTask(TASK_ID, form, bindingResult);
      assertViewName(modelAndView, "tasks/edit");
      assertModelAttributeValue(modelAndView, "taskForm", form);
   }

   @Test(expected = NotFoundException.class)
   public void postEditTaskPropagatesException() {
      when(taskService.get(TASK_ID)).thenThrow(new NotFoundException("task"));
      controller.postEditTask(TASK_ID, new TaskForm(), bindingResult);
   }

   @Rule
   public MockitoRule mockitoRule() {
      return MockitoJUnit.rule();
   }

}
