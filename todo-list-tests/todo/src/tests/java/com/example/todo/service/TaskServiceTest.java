package com.example.todo.service;

import com.example.todo.models.Task;
import com.example.todo.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    public TaskServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTasks() {
        when(taskRepository.findAll()).thenReturn(Arrays.asList(
                new Task("Task 1", false),
                new Task("Task 2", true)
        ));

        var tasks = taskService.getAllTasks();
        assertEquals(2, tasks.size());
    }

    @ParameterizedTest
    @MethodSource("provideTasksForGetTaskById")
    void testGetTaskById(Long id, Task task, boolean shouldBePresent) {
        when(taskRepository.findById(id)).thenReturn(Optional.ofNullable(task));

        var result = taskService.getTaskById(id);

        assertEquals(shouldBePresent, result.isPresent());
        if (shouldBePresent) {
            assertEquals(task.getDescription(), result.get().getDescription());
        }
    }

    static Stream<Arguments> provideTasksForGetTaskById() {
        return Stream.of(
                Arguments.of(1L, new Task("Test Task 1", false), true),
                Arguments.of(2L, null, false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTasksForCreateTask")
    void testCreateTask(Task inputTask, Task expectedTask) {
        when(taskRepository.save(inputTask)).thenReturn(expectedTask);

        var createdTask = taskService.createTask(inputTask);

        assertNotNull(createdTask);
        assertEquals(expectedTask.getDescription(), createdTask.getDescription());
    }

    static Stream<Arguments> provideTasksForCreateTask() {
        return Stream.of(
                Arguments.of(new Task("New Task", false), new Task("New Task", false)),
                Arguments.of(new Task("Another Task", true), new Task("Another Task", true))
        );
    }

    @ParameterizedTest
    @MethodSource("provideIdsForDeleteTask")
    void testDeleteTask(Long id) {
        doNothing().when(taskRepository).deleteById(id);

        taskService.deleteTask(id);

        verify(taskRepository, times(1)).deleteById(id);
    }

    static Stream<Arguments> provideIdsForDeleteTask() {
        return Stream.of(
                Arguments.of(1L),
                Arguments.of(2L)
        );
    }
}