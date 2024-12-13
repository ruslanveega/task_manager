package com.ruslan.springboot.task_manager.controller;

import com.ruslan.springboot.task_manager.entity.Task;
import com.ruslan.springboot.task_manager.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Integer id, @RequestBody Task task) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        try {
            Task result = taskService.updateTask(id, task, currentUser, isAdmin);
            return ResponseEntity.ok(result);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Integer id) {
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        taskService.deleteTask(id, isAdmin);
    }

    @GetMapping
    public Page<Task> getTasks(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String assignee,
            Pageable pageable)
    {
        return taskService.getTasks(author, assignee, pageable);
    }
}
