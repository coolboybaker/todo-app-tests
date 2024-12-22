package com.example.todo.controllers;

import com.example.todo.models.Task;
import com.example.todo.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @Autowired
    private MockMvc mockMvc;

    public TaskControllerTest() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @ParameterizedTest
    @MethodSource("provideTasksForGetAll")
    void testGetAllTasks(List<Task> mockTasks, int expectedSize) throws Exception {
        when(taskService.getAllTasks()).thenReturn(mockTasks);

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedSize));
    }

    static Stream<Arguments> provideTasksForGetAll() {
        return Stream.of(
                Arguments.of(Arrays.asList(new Task("Task 1", false), new Task("Task 2", true)), 2),
                Arguments.of(Arrays.asList(new Task("Task 1", false)), 1),
                Arguments.of(Arrays.asList(), 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTasksForCreate")
    void testCreateTask(Task inputTask, Task returnedTask, String expectedDescription) throws Exception {
        when(taskService.createTask(any(Task.class))).thenReturn(returnedTask);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(inputTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(expectedDescription));
    }

    static Stream<Arguments> provideTasksForCreate() {
        return Stream.of(
                Arguments.of(new Task("New Task", false), new Task("New Task", false), "New Task"),
                Arguments.of(new Task("Another Task", true), new Task("Another Task", true), "Another Task")
        );
    }
}