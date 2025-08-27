package com.example.simpletodo.repository;

import com.example.simpletodo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    // Find todos by completion status
    List<Todo> findByCompleted(Boolean completed);

    // Find todos ordered by creation date (newest first)
    List<Todo> findAllByOrderByCreatedAtDesc();

    // Find todos by completion status ordered by creation date
    List<Todo> findByCompletedOrderByCreatedAtDesc(Boolean completed);

    // Count todos by completion status
    long countByCompleted(Boolean completed);

    // Custom query to find todos containing specific text (case-insensitive)
    @Query("SELECT t FROM Todo t WHERE LOWER(t.task) LIKE LOWER(CONCAT('%', ?1, '%')) ORDER BY t.createdAt DESC")
    List<Todo> findByTaskContainingIgnoreCase(String searchText);
}
