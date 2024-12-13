package com.ruslan.springboot.task_manager.service;

import com.ruslan.springboot.task_manager.entity.Comment;
import com.ruslan.springboot.task_manager.entity.Task;
import com.ruslan.springboot.task_manager.repository.CommentRepository;
import com.ruslan.springboot.task_manager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    public CommentService(CommentRepository commentRepository, TaskRepository taskRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
    }

    public Comment addComment(Integer taskId, Comment comment, String currentUser, boolean isAdmin) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!isAdmin && !currentUser.equals(task.getAssignee())) {
            throw new SecurityException("You are not authorized to add a comment to this task!");
        }

        comment.setTask(task);
        comment.setAuthor(currentUser);
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByTaskId(Integer taskId) {
        return commentRepository.findByTaskId(taskId);
    }
}
