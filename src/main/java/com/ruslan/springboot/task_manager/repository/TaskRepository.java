package com.ruslan.springboot.task_manager.repository;

import com.ruslan.springboot.task_manager.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    Page<Task> findByAuthor(String author, Pageable pageable);

    Page<Task> findByAssignee(String assignee, Pageable pageable);

    Page<Task> findByAuthorAndAssignee(String author, String assignee, Pageable pageable);
}
