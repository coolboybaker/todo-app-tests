package com.example.todo.integration;

import com.example.todo.models.Task;
import com.example.todo.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TaskIntegrationTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void testCreateTask() {
        Task task = new Task("Integration Test Task", false);
        Task savedTask = taskRepository.save(task);
        assertNotNull(savedTask.getId());
    }

    @Test
    void testFindAllTasks() {
        taskRepository.save(new Task("Task 1", false));
        taskRepository.save(new Task("Task 2", true));

        List<Task> tasks = taskRepository.findAll();
        assertTrue(tasks.size() >= 2);
    }

    @Test
    void testUpdateTask() {
        Task task = taskRepository.save(new Task("Task to Update", false));
        task.setCompleted(true);

        Task updatedTask = taskRepository.save(task);
        assertTrue(updatedTask.isCompleted());
    }

    @Test
    void testDeleteTask() {
        Task task = taskRepository.save(new Task("Task to Delete", false));
        taskRepository.deleteById(task.getId());

        assertFalse(taskRepository.findById(task.getId()).isPresent());
    }
}