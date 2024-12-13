package com.ruslan.springboot.task_manager.service;

import com.ruslan.springboot.task_manager.entity.Task;
import com.ruslan.springboot.task_manager.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Integer id, Task updatedTask, String currentUser, boolean isAdmin) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        if (isAdmin) {
            existingTask.setTitle(updatedTask.getTitle());
            existingTask.setDescription(updatedTask.getDescription());
            existingTask.setStatus(updatedTask.getStatus());
            existingTask.setPriority(updatedTask.getPriority());
            existingTask.setAssignee(updatedTask.getAssignee());
        } else {
            if (!existingTask.getAssignee().equals(currentUser)) {
                throw new SecurityException("You are not the assignee of this task!");
            }
            existingTask.setStatus(updatedTask.getStatus());
        }

        return taskRepository.save(existingTask);
    }

    public void deleteTask(Integer id, boolean isAdmin) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        if (!isAdmin) {
            throw new SecurityException("Only admin can delete tasks!");
        }

        taskRepository.delete(task);
    }

    public Page<Task> getTasks(String author, String assignee, Pageable pageable) {
        if (author != null && assignee != null) {
            return taskRepository.findByAuthorAndAssignee(author, assignee, pageable);
        } else if (author != null) {
            return taskRepository.findByAuthor(author, pageable);
        } else if (assignee != null) {
            return taskRepository.findByAssignee(assignee, pageable);
        } else {
            return taskRepository.findAll(pageable);
        }
    }
}

