package com.ruslan.springboot.task_manager.repository;

import com.ruslan.springboot.task_manager.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByTaskId(Integer taskId);
}
